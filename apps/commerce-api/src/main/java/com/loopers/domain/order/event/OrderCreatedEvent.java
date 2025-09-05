package com.loopers.domain.order.event;

import com.loopers.domain.event.DomainEvent;
import com.loopers.domain.order.Order;

public record OrderCreatedEvent(
		Long userId,
		Long orderId,
		int finalPrice,
		int discountAmount,
		Long couponId
) implements DomainEvent {
	public static OrderCreatedEvent from(Order order) {
		return new OrderCreatedEvent(
				order.getUserId(),
				order.getId(),
				order.getFinalPrice(),
				order.getDiscountAmount(),
				order.getCouponId()
		);
	}
}
