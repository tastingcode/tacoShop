package com.loopers.domain.payment;

public interface PaymentGatewayPort {
	PaymentInfo.PaymentResponse requestPayment(PaymentInfo.PaymentRequest paymentRequest);

	PaymentInfo.PaymentResponse requestPaymentInfo(String transactionKey);
}
