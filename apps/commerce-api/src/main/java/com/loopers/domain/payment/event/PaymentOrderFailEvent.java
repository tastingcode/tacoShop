package com.loopers.domain.payment.event;

import com.loopers.domain.event.DomainEvent;

public record PaymentOrderFailEvent(
		Long paymentId,
		String userId,
		Long orderId
) implements DomainEvent {
	public static PaymentOrderFailEvent of(Long paymentId, String userId, Long orderId) {
		return new PaymentOrderFailEvent(paymentId, userId, orderId);
	}
}
