package com.loopers.domain.payment.event;

public record PaymentOrderSuccessEvent(
		String userId,
		Long orderId
) {
	public static PaymentOrderSuccessEvent of(String userId, Long orderId) {
		return new PaymentOrderSuccessEvent(userId, orderId);
	}
}
