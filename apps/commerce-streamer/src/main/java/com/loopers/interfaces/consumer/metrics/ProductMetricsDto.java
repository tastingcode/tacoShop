package com.loopers.interfaces.consumer.metrics;

import com.loopers.application.metrics.ProductMetricsCommand;

public record ProductMetricsDto(
		Long ProductId,
		String eventType,
		int delta
) {
	public ProductMetricsCommand toCommand() {
		return new ProductMetricsCommand(ProductId, eventType, delta);
	}
}
