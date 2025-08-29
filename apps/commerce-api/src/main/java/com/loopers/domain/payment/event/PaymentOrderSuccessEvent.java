package com.loopers.domain.payment.event;

public record PaymentOrderSuccessEvent(
		Long paymentId,
		String userId,
		Long orderId
) {
	public static PaymentOrderSuccessEvent of(Long paymentId, String userId, Long orderId) {
		return new PaymentOrderSuccessEvent(paymentId, userId, orderId);
	}
}
