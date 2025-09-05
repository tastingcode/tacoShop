package com.loopers.domain.payment.event;

import com.loopers.domain.event.DomainEvent;

public record PaymentOrderSuccessEvent(
		Long paymentId,
		String userId,
		Long orderId
) implements DomainEvent {
	public static PaymentOrderSuccessEvent of(Long paymentId, String userId, Long orderId) {
		return new PaymentOrderSuccessEvent(paymentId, userId, orderId);
	}
}
