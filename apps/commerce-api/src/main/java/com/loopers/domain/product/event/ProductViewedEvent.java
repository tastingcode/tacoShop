package com.loopers.domain.product.event;

import com.loopers.domain.event.DomainEvent;

public record ProductViewedEvent(
		Long productId
) implements DomainEvent {
	public static ProductViewedEvent of(Long productId) {
		return new ProductViewedEvent(productId);
	}
}
