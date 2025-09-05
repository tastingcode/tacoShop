package com.loopers.domain.metrics;

import java.util.List;

public interface ProductMetricsRepository {
	List<ProductMetrics> saveAll(List<ProductMetrics> productMetricsList);
}
