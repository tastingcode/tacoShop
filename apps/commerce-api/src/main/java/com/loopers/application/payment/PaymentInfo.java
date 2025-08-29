package com.loopers.application.payment;


import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.domain.payment.PaymentType;

public record PaymentInfo(
		Long paymentId,
		String transactionKey,
		String userId,
		Long orderId,
		int amount,
		PaymentType paymentType,
		PaymentStatus paymentStatus
) {
	public static PaymentInfo from(Payment payment) {
		return new PaymentInfo(payment.getId(),
				payment.getTransactionKey(),
				payment.getUserId(),
				payment.getOrderId(),
				payment.getAmount(),
				payment.getPaymentType(),
				payment.getPaymentStatus());
	}
}
