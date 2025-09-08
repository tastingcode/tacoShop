package com.loopers.application.metrics;

import com.loopers.domain.like.event.LikeEventType;
import com.loopers.domain.like.event.ProductLikeEvent;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderDomainService;
import com.loopers.domain.order.OrderProduct;
import com.loopers.domain.payment.event.PaymentOrderSuccessEvent;
import com.loopers.domain.product.event.ProductViewedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductMetricsEventHandler {

	@Value("${kafka.topic.product-metrics}")
	private String productMetricsTopic;

	private final KafkaTemplate<Object, Object> kafkaTemplate;

	private final OrderDomainService orderDomainService;

	public void handleLikeMetrics(ProductLikeEvent event){
		int delta = event.likeEventType().equals(LikeEventType.LIKE) ? 1 : -1;
		ProductMetricsCommand productMetricsCommand = ProductMetricsCommand.of(event.eventId(), event.productId(), event.getEventType(), delta);

		sendMetrics(productMetricsCommand);
	}

	public void handleStockMetrics(PaymentOrderSuccessEvent event){
		Order order = orderDomainService.getOrder(event.orderId());
		List<OrderProduct> orderProducts = order.getOrderProducts();
		List<ProductMetricsCommand> productMetricsCommandList = orderProducts.stream()
				.map(item -> ProductMetricsCommand.of(event.eventId(), item.getProductId(), event.getEventType(), item.getQuantity()))
				.toList();

		for (ProductMetricsCommand productMetricsCommand : productMetricsCommandList) {
			sendMetrics(productMetricsCommand);
		}
	}

	public void handleViewMetrics(ProductViewedEvent event){
		ProductMetricsCommand productMetricsCommand = ProductMetricsCommand.of(event.eventId(), event.productId(), event.getEventType(), 1);

		sendMetrics(productMetricsCommand);
	}

	public void sendMetrics(ProductMetricsCommand productMetricsCommand){
		kafkaTemplate.send(productMetricsTopic, productMetricsCommand);
	}

}
