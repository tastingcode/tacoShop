package com.loopers.application.order;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderStatus;
import lombok.ToString;


public record OrderInfo(
        Long orderId,
        int finalPrice,
		int discountAmount,
        OrderStatus status
) {

    public static OrderInfo from(Order order) {
        return new OrderInfo(
                order.getId(),
				order.getFinalPrice(),
				order.getDiscountAmount(),
				order.getStatus()
        );
    }

}
