package com.loopers.domain.product.event;

import com.loopers.domain.event.DomainEvent;

import java.util.UUID;

public record ProductViewedEvent(
		String eventId,
		Long productId
) implements DomainEvent {
	public static ProductViewedEvent of(Long productId) {
		return new ProductViewedEvent(UUID.randomUUID().toString(), productId);
	}
}
