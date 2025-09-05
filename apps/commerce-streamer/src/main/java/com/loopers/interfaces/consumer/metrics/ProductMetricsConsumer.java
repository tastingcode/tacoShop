package com.loopers.interfaces.consumer.metrics;

import com.loopers.application.metrics.ProductMetricsCommand;
import com.loopers.application.metrics.ProductMetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductMetricsConsumer {

	private final ProductMetricsService productMetricsService;

	@KafkaListener(
			topics = "${kafka.topic.product-metrics}",
			groupId = "${kafka.group.product-metrics-group-id}",
			concurrency = "3"
	)
	public void consume(ProductMetricsDto productMetricsDto) {
		ProductMetricsCommand command = productMetricsDto.toCommand();
		productMetricsService.handleMetrics(command);
	}

}
