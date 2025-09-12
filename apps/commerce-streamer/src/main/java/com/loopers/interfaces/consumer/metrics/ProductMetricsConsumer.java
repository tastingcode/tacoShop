package com.loopers.interfaces.consumer.metrics;

import com.loopers.application.metrics.MetricsFacade;
import com.loopers.application.metrics.ProductMetricsCommand;
import com.loopers.confg.kafka.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductMetricsConsumer {

	private final MetricsFacade metricsFacade;

	@KafkaListener(
			topics = "${kafka.topic.product-metrics}",
			groupId = "${kafka.group.product-metrics-group-id}",
			containerFactory = KafkaConfig.BATCH_LISTENER
	)
	public void consume(List<ProductMetricsDto> productMetricsDtoList, Acknowledgment ack) {
		List<ProductMetricsCommand> commands = productMetricsDtoList.stream()
				.map(ProductMetricsDto::toCommand)
				.toList();

		metricsFacade.processMetrics(commands);
		ack.acknowledge();
	}

}
