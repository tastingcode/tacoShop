package com.loopers.application.metrics;

public record ProductMetricsCommand(
		Long ProductId,
		String eventType,
		int delta
) {
}
