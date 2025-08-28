package com.loopers.application.payment;

import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentDto;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.domain.payment.PaymentStrategy;
import com.loopers.domain.payment.event.PaymentRequestFailEvent;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardPaymentStrategy implements PaymentStrategy {
	private final PaymentGatewayService paymentGatewayService;
	private final PaymentService paymentService;
	private final ApplicationEventPublisher eventPublisher;

	@Override
	public PaymentInfo pay(PaymentCommand command) {
		// 결제 요청 응답
		PaymentDto.PaymentResponse paymentResponse;

		// 결제 요청
		try {
			paymentResponse = paymentGatewayService.requestPayment(command);
		} catch (Exception e) {
			eventPublisher.publishEvent(PaymentRequestFailEvent.of(command.userId(), command.orderId()));
			throw new CoreException(ErrorType.INTERNAL_ERROR, " 현재 모든 PG 사의 서버가 불안정합니다. 잠시 후 다시 시도해주세요.");
		}

		// 결제 생성
		Payment payment = paymentService.createPayment(paymentResponse.data().transactionKey(), command, PaymentStatus.PENDING);

		return PaymentInfo.from(payment);
	}

}
