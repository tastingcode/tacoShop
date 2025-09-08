package com.loopers.domain.order.event;

import com.loopers.domain.event.DomainEvent;
import com.loopers.domain.order.Order;

import java.util.UUID;

public record OrderCreatedEvent(
		String eventId,
		Long userId,
		Long orderId,
		int finalPrice,
		int discountAmount,
		Long couponId
) implements DomainEvent {
	public static OrderCreatedEvent from(Order order) {
		return new OrderCreatedEvent(
				UUID.randomUUID().toString(),
				order.getUserId(),
				order.getId(),
				order.getFinalPrice(),
				order.getDiscountAmount(),
				order.getCouponId()
		);
	}
}
