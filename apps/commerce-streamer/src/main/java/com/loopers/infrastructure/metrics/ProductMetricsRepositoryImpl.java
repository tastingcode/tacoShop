package com.loopers.infrastructure.metrics;

import com.loopers.domain.metrics.ProductMetrics;
import com.loopers.domain.metrics.ProductMetricsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductMetricsRepositoryImpl implements ProductMetricsRepository {
	private final ProductMetricsJpaRepository productMetricsJpaRepository;

	@Override
	public ProductMetrics save(ProductMetrics productMetrics) {
		return productMetricsJpaRepository.save(productMetrics);
	}

	@Override
	public Optional<ProductMetrics> findByProductIdAndDate(Long productId, LocalDate date) {
		return productMetricsJpaRepository.findByProductIdAndDate(productId, date);
	}
}
