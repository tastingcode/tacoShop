package com.loopers.application.order.event;

import com.loopers.domain.dataplatform.DataPlatformPort;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderDomainService;
import com.loopers.domain.order.OrderProduct;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.event.PaymentOrderSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderEventHandler {
	private final OrderDomainService orderDomainService;
	private final DataPlatformPort dataPlatformPort;

	@Transactional
	public void failOrder(Long orderId) {
		// 주문 조회
		Order order = orderDomainService.getOrder(orderId);

		// 재고 복구
		List<OrderProduct> orderProducts = order.getOrderProducts();
		orderDomainService.restoreStocks(orderProducts);

		// 주문 실패
		order.updateStatus(OrderStatus.FAILURE);
	}

	@Transactional
	public void completeOrder(PaymentOrderSuccessEvent event) {
		// 주문 조회
		Order order = orderDomainService.getOrder(event.orderId());

		// 주문 완료
		order.updateStatus(OrderStatus.COMPLETED);
	}

}
