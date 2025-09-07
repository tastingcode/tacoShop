package com.loopers.domain.payment.event;

import com.loopers.domain.event.DomainEvent;

import java.util.UUID;

public record PaymentOrderFailEvent(
		String eventId,
		Long paymentId,
		String userId,
		Long orderId
) implements DomainEvent {
	public static PaymentOrderFailEvent of(Long paymentId, String userId, Long orderId) {
		return new PaymentOrderFailEvent(UUID.randomUUID().toString(), paymentId, userId, orderId);
	}
}
