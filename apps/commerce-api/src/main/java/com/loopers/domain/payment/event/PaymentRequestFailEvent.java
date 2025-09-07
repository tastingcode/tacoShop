package com.loopers.domain.payment.event;

import com.loopers.domain.event.DomainEvent;

import java.util.UUID;

public record PaymentRequestFailEvent(
		String eventId,
		String userId,
		Long orderId
) implements DomainEvent {
	public static PaymentRequestFailEvent of(String userId, Long orderId) {
		return new PaymentRequestFailEvent(UUID.randomUUID().toString(), userId, orderId);
	}
}
