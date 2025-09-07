package com.loopers.application.metrics;

public record ProductMetricsCommand(
		String eventId,
		Long ProductId,
		String eventType,
		int delta
) {
}
