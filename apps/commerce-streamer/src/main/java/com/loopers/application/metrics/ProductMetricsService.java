package com.loopers.application.metrics;

import com.loopers.domain.metrics.ProductMetricType;
import com.loopers.domain.metrics.ProductMetrics;
import com.loopers.domain.metrics.ProductMetricsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMetricsService {

	private final ProductMetricsRepository productMetricsRepository;

	public void handleMetrics(List<ProductMetricsCommand> commands) {
		List<ProductMetrics> productMetricsList = commands.stream()
				.map(c -> ProductMetrics.create(c.ProductId(),
						c.eventType(),
						ProductMetricType.from(c.eventType()),
						c.delta()))
				.toList();

		productMetricsRepository.saveAll(productMetricsList);

	}


}
