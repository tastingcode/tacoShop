package com.loopers.application.payment;


import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCallbackRequest;
import com.loopers.domain.payment.PaymentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentFacade {
	private final PaymentGatewayService paymentGatewayService;
	private final PaymentService paymentService;
	public void checkout(PaymentCommand command){
		// 유저 유효성 검증
		paymentService.validateOrder(command);

		// 결제 요청
		PaymentInfo.PaymentResponse paymentResponse = paymentGatewayService.requestPayment(command);

		// 결제 생성
		paymentService.createPayment(paymentResponse, command);
	}

	public void callback(PaymentCallbackRequest paymentCallbackRequest) {
		// 결제 트랜잭션 유효성 검증
		paymentGatewayService.validatePaymentTransaction(paymentCallbackRequest.transactionKey());

		// 결제 유효성 검증
		Payment payment = paymentService.getValidatedPayment(paymentCallbackRequest);

		// 결제 및 주문 업데이트
		paymentService.updatePaymentAndOrder(payment, paymentCallbackRequest.status());
	}

}
