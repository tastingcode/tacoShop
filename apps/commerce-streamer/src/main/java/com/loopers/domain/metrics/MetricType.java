package com.loopers.domain.metrics;

public enum MetricType {
	PRODUCT_LIKE("ProductLikeEvent"),
	PRODUCT_UNLIKE("ProductUnlikeEvent"),
	PRODUCT_VIEWED("ProductViewedEvent"),
	PRODUCT_SALES("PaymentOrderSuccessEvent");

	private final String eventType;
	MetricType(String eventType) {
		this.eventType = eventType;
	}

	public static MetricType from(String eventType) {
		for (MetricType type : values()) {
			if (type.eventType.equals(eventType)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid event type: " + eventType);
	}
}
