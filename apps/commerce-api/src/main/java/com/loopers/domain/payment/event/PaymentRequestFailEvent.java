package com.loopers.domain.payment.event;

import com.loopers.domain.event.DomainEvent;

public record PaymentRequestFailEvent(
		String userId,
		Long orderId
) implements DomainEvent {
	public static PaymentRequestFailEvent of(String userId, Long orderId) {
		return new PaymentRequestFailEvent(userId, orderId);
	}
}
