package com.loopers.interfaces.event.outbound;

import com.loopers.application.metrics.ProductMetricsEventHandler;
import com.loopers.domain.like.event.ProductLikeEvent;
import com.loopers.domain.payment.event.PaymentOrderSuccessEvent;
import com.loopers.domain.product.event.ProductViewedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMetricsEventListener {

	private final ProductMetricsEventHandler productMetricsEventHandler;

	@EventListener
	public void handle(ProductLikeEvent event) {
		productMetricsEventHandler.handleLikeMetrics(event);
	}

	@EventListener
	public void handle(PaymentOrderSuccessEvent event) {
		productMetricsEventHandler.handleStockMetrics(event);
	}

	@EventListener
	public void handle(ProductViewedEvent event) {
		productMetricsEventHandler.handleViewMetrics(event);
	}
}
