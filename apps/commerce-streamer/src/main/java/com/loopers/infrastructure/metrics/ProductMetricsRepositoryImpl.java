package com.loopers.infrastructure.metrics;

import com.loopers.domain.metrics.ProductMetrics;
import com.loopers.domain.metrics.ProductMetricsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMetricsRepositoryImpl implements ProductMetricsRepository {
	private final ProductMetricsJpaRepository productMetricsJpaRepository;

	@Override
	public List<ProductMetrics> saveAll(List<ProductMetrics> productMetricsList) {
		return productMetricsJpaRepository.saveAll(productMetricsList);
	}
}
