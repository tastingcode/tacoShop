package com.loopers.domain.metrics;

public enum ProductMetricType {
	PRODUCT_LIKED("ProductLikeEvent"),
	PRODUCT_VIEWED("ProductViewedEvent"),
	PRODUCT_SALES("PaymentOrderSuccessEvent");

	private final String eventType;
	ProductMetricType(String eventType) {
		this.eventType = eventType;
	}

	public static ProductMetricType from(String eventType) {
		for (ProductMetricType type : values()) {
			if (type.eventType.equals(eventType)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid event type: " + eventType);
	}
}
