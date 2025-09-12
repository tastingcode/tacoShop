package com.loopers.domain.metrics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ProductMetricsDomainService {

	private final ProductMetricsRepository productMetricsRepository;

	public ProductMetrics getProductMetrics(Long productId){
		return productMetricsRepository.findByProductIdAndDate(productId, LocalDate.now())
				.orElseGet(() -> ProductMetrics.of(productId));
	}

	public void updateMetricsDeltas(ProductMetrics productMetrics, MetricType metricType, int delta) {
		switch (metricType) {
			case PRODUCT_LIKE, PRODUCT_UNLIKE -> productMetrics.adjustLikesDelta(delta);
			case PRODUCT_SALES -> productMetrics.increaseSalesDelta(delta);
			case PRODUCT_VIEWED -> productMetrics.increaseViewsDelta(delta);
		}
	}

	@Transactional
	public ProductMetrics saveMetrics(ProductMetrics productMetrics){
		return productMetricsRepository.save(productMetrics);
	}
}
