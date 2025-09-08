package com.loopers.application.metrics;

public record ProductMetricsCommand(
		String eventId,
		Long ProductId,
		String eventType,
		int delta
) {
	public static ProductMetricsCommand of(String eventId, Long productId, String eventType, int delta) {
		return new ProductMetricsCommand(eventId, productId, eventType, delta);
	}
}
