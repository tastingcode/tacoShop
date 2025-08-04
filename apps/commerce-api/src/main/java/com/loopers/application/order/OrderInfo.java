package com.loopers.application.order;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderStatus;

public record OrderInfo(
        Long orderId,
        int totalPrice,
        OrderStatus status
) {

    public static OrderInfo from(Order order) {
        return new OrderInfo(
                order.getId(),
				order.getTotalPrice(),
				order.getStatus()
        );
    }

}
