package com.loopers.infrastructure.payment.pg;

import com.loopers.domain.payment.PaymentGatewayPort;
import com.loopers.domain.payment.PaymentInfo;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;


@Slf4j
@RequiredArgsConstructor
@Component
public class LuckyPgAdapter implements PaymentGatewayPort {

	private final LuckyPgClient luckyPgClient;

	private static final String CALLBACK_URL = "http://localhost:8080/api/v1/payments/callback";
	private static final Long MERCHANT_ID = 135135L;


	@Override
	@Retry(name = "pgRetry", fallbackMethod = "fallback")
	@CircuitBreaker(name = "pgCircuit", fallbackMethod = "circuitFallback")
	public PaymentInfo.PaymentResponse requestPayment(PaymentInfo.PaymentRequest paymentRequest) {
		PgDto.PgRequest pgRequest = PgDto.PgRequest.from(paymentRequest, CALLBACK_URL);
		PgDto.PgResponse pgResponse = luckyPgClient.requestPayment(MERCHANT_ID, pgRequest);
		return PaymentInfo.PaymentResponse.of(pgResponse);
	}

	@Override
	@Retry(name = "pgRetry", fallbackMethod = "retryFallback")
	public PaymentInfo.PaymentResponse requestPaymentInfo(String transactionKey) {
		PgDto.PgResponse pgResponse = luckyPgClient.requestPaymentInfo(MERCHANT_ID, transactionKey);
		return PaymentInfo.PaymentResponse.of(pgResponse);
	}

	public PaymentInfo.PaymentResponse circuitFallback(PaymentInfo.PaymentRequest paymentRequest, Throwable t) {
		log.error("[CircuitFallback] - orderId={}, reason={}", paymentRequest.orderId(), t.getMessage());

		log.error("LuckyPG 불가 -> 백업 PG 시도");
		// 다른 PG 사로 연결 요청

		throw new CoreException(ErrorType.INTERNAL_ERROR, "[CircuitFallback] - PG 연동 중 오류 발생");
	}


	public PaymentInfo.PaymentResponse retryFallback(PaymentInfo.PaymentRequest paymentRequest, Throwable t) {
		log.error("[RetryFallback] - orderId={}, reason={}", paymentRequest.orderId(), t.getMessage());
		if (t instanceof RetryableException || t instanceof SocketTimeoutException) {
			log.error("네트워크 지연으로 인한 결제 실패");
			return PaymentInfo.PaymentResponse.fallback("네트워크 지연으로 인한 결제 실패, 잠시 후 다시 시도해주세요.");
		}

		throw new CoreException(ErrorType.INTERNAL_ERROR, "[RetryFallback] - PG 연동 중 오류 발생");
	}

}
