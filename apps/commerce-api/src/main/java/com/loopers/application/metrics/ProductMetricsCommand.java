package com.loopers.application.metrics;

public record ProductMetricsCommand(
		Long ProductId,
		String eventType,
		int delta
) {
	public static ProductMetricsCommand of(Long productId, String eventType, int delta) {
		return new ProductMetricsCommand(productId, eventType, delta);
	}
}
