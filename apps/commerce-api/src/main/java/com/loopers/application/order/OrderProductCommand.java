package com.loopers.application.order;

import com.loopers.domain.order.OrderProduct;

import java.util.List;

public record OrderProductCommand(
		Long productId,
		int price,
		int quantity
) {

	public static List<OrderProduct> toOrderProducts(List<OrderProductCommand> commands) {
		return commands.stream()
				.map(c -> OrderProduct.of(null, c.productId, c.price, c.quantity))
				.toList();
	}

}
