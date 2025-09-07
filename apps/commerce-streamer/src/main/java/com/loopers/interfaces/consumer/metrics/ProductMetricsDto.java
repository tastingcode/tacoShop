package com.loopers.interfaces.consumer.metrics;

import com.loopers.application.metrics.ProductMetricsCommand;

public record ProductMetricsDto(
		String eventId,
		Long ProductId,
		String eventType,
		int delta
) {
	public ProductMetricsCommand toCommand() {
		return new ProductMetricsCommand(eventId, ProductId, eventType, delta);
	}
}
