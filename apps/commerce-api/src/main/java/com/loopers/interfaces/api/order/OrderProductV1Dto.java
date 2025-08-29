package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderProductCommand;

public class OrderProductV1Dto {
	public record OrderProductRequest(
			Long productId,
			int price,
			int quantity
	) {
		public OrderProductCommand toCommand() {
			return new OrderProductCommand(productId, price, quantity);
		}
	}
}
