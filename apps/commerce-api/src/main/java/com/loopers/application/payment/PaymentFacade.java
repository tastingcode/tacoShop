package com.loopers.application.payment;


import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCallbackRequest;
import com.loopers.domain.payment.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentFacade {
	private final PaymentGatewayService paymentGatewayService;
	private final PaymentService paymentService;
	private final PaymentStrategyFactory paymentStrategyFactory;

	public PaymentInfo checkout(PaymentCommand command) {
		// 유저 주문 유효성 검증
		paymentService.validateUserOrder(command);

		// 결제 전략 선택
		PaymentStrategy paymentStrategy = paymentStrategyFactory.getPaymentStrategy(command.paymentType());

		// 결제
		return paymentStrategy.pay(command);
	}

	public PaymentInfo callback(PaymentCallbackRequest paymentCallbackRequest) {
		// 결제 트랜잭션 유효성 검증
		paymentGatewayService.validatePaymentTransaction(paymentCallbackRequest.transactionKey());

		// 결제 유효성 검증
		Payment payment = paymentService.getValidatedPayment(paymentCallbackRequest);

		// 이벤트 기반 결제 및 주문 동기화
		return paymentService.syncPaymentOrder(payment, paymentCallbackRequest.status());
	}

}
