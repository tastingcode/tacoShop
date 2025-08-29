package com.loopers.domain.payment.event;

public record PaymentOrderFailEvent(
		Long paymentId,
		String userId,
		Long orderId
) {
	public static PaymentOrderFailEvent of(Long paymentId, String userId, Long orderId) {
		return new PaymentOrderFailEvent(paymentId, userId, orderId);
	}
}
