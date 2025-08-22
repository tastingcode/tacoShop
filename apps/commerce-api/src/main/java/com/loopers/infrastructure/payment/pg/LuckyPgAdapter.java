package com.loopers.infrastructure.payment.pg;

import com.loopers.domain.payment.PaymentGatewayPort;
import com.loopers.domain.payment.PaymentInfo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class LuckyPgAdapter implements PaymentGatewayPort {

	private final LuckyPgClient luckyPgClient;

	private static final String CALLBACK_URL = "http://localhost:8080/api/v1/payments/callback";
	private static final Long MERCHANT_ID = 135135L;


	@Override
	@Retry(name = "pgRetry", fallbackMethod = "fallback")
	@CircuitBreaker(name = "pgCircuit", fallbackMethod = "fallback")
	public PaymentInfo.PaymentResponse requestPayment(PaymentInfo.PaymentRequest paymentRequest) {
		PgDto.PgRequest pgRequest = PgDto.PgRequest.from(paymentRequest, CALLBACK_URL);
		PgDto.PgResponse pgResponse = luckyPgClient.requestPayment(MERCHANT_ID, pgRequest);
		return PaymentInfo.PaymentResponse.of(pgResponse);
	}

	@Override
	@Retry(name = "pgRetry", fallbackMethod = "fallback")
	public PaymentInfo.PaymentResponse requestPaymentInfo(String transactionKey) {
		PgDto.PgResponse pgResponse = luckyPgClient.requestPaymentInfo(MERCHANT_ID, transactionKey);
		return PaymentInfo.PaymentResponse.of(pgResponse);
	}

	public PaymentInfo.PaymentResponse fallback(Throwable t) {
		PaymentInfo.PaymentResponse failResponse = PaymentInfo.PaymentResponse.fallback();
		log.info("##### fallback 메서드 호출 {} ", failResponse.meta().result());
		return failResponse;
	}

}
