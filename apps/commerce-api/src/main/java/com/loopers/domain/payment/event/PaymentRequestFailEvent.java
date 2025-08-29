package com.loopers.domain.payment.event;

public record PaymentRequestFailEvent (
		String userId,
		Long orderId
){
	public static PaymentRequestFailEvent of(String userId, Long orderId) {
		return new PaymentRequestFailEvent(userId, orderId);
	}
}
