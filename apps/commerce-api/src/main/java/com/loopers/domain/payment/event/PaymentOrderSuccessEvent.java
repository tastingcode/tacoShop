package com.loopers.domain.payment.event;

import com.loopers.domain.event.DomainEvent;

import java.util.UUID;

public record PaymentOrderSuccessEvent(
		String eventId,
		Long paymentId,
		String userId,
		Long orderId
) implements DomainEvent {
	public static PaymentOrderSuccessEvent of(Long paymentId, String userId, Long orderId) {
		return new PaymentOrderSuccessEvent(UUID.randomUUID().toString(), paymentId, userId, orderId);
	}
}
