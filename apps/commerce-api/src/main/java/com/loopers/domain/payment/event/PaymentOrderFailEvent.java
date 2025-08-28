package com.loopers.domain.payment.event;

public record PaymentOrderFailEvent(
		String userId,
		Long orderId
) {
	public static PaymentOrderFailEvent of(String userId, Long orderId) {
		return new PaymentOrderFailEvent(userId, orderId);
	}
}
