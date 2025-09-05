package com.loopers.application.metrics;

import com.loopers.domain.metrics.MetricType;
import com.loopers.domain.metrics.ProductMetrics;
import com.loopers.domain.metrics.ProductMetricsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ProductMetricsService {

	private final ProductMetricsRepository productMetricsRepository;

	@Transactional
	public void handleMetrics(ProductMetricsCommand command) {

		ProductMetrics productMetrics = productMetricsRepository.findByProductIdAndDate(command.ProductId(), LocalDate.now())
				.orElseGet(() -> ProductMetrics.of(command.ProductId()));

		MetricType metricType = MetricType.from(command.eventType());
		switch (metricType) {
			case PRODUCT_LIKE -> productMetrics.adjustLikesDelta(command.delta());
			case PRODUCT_SALES -> productMetrics.increaseSalesDelta(command.delta());
			case PRODUCT_VIEWED -> productMetrics.increaseViewsDelta(command.delta());
		}
		productMetricsRepository.save(productMetrics);
	}
}
