package com.loopers.domain.metrics;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductMetricsRepository {
	ProductMetrics save(ProductMetrics productMetrics);
	Optional<ProductMetrics> findByProductIdAndDate(Long productId, LocalDate date);
}
