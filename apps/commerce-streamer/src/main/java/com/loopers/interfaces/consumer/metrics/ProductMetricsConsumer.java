package com.loopers.interfaces.consumer.metrics;

import com.loopers.application.metrics.ProductMetricsCommand;
import com.loopers.application.metrics.ProductMetricsService;
import com.loopers.confg.kafka.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductMetricsConsumer {

	private final ProductMetricsService productMetricsService;

	@KafkaListener(
			topics = "${kafka.topic.product-metrics}",
			groupId = "${kafka.group.product-metrics-group-id}",
			containerFactory = KafkaConfig.BATCH_LISTENER
	)
	public void consume(List<ProductMetricsDto> records) {
		List<ProductMetricsCommand> commands = records.stream()
				.map(ProductMetricsDto::toCommand)
				.toList();

		productMetricsService.handleMetrics(commands);
	}

}
