package com.loopers.domain.payment;

public interface PaymentGatewayPort {
	PaymentDto.PaymentResponse requestPayment(PaymentDto.PaymentRequest paymentRequest);

	PaymentDto.PaymentResponse requestPaymentInfo(String transactionKey);
}
