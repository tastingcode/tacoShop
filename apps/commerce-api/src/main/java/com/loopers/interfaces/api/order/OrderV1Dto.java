package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderCommand;
import com.loopers.application.order.OrderInfo;
import com.loopers.application.order.OrderProductCommand;
import com.loopers.domain.order.OrderStatus;

import java.util.List;

public class OrderV1Dto {

	public record OrderRequest(
			List<OrderProductV1Dto.OrderProductRequest> orderProducts,
			Long couponId
	) {
		public OrderCommand toCommand(String userId) {
			List<OrderProductCommand> orderProductCommands = orderProducts.stream()
					.map(OrderProductV1Dto.OrderProductRequest::toCommand)
					.toList();
			return new OrderCommand(userId, orderProductCommands, couponId);
		}
	}

	public record OrderResponse(
			Long orderId,
			int finalPrice,
			int discountAmount,
			OrderStatus status
	) {
		public static OrderResponse from(OrderInfo info) {
			return new OrderResponse(info.orderId(), info.finalPrice(), info.discountAmount(), info.status());
		}
	}
}
