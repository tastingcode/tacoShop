package com.loopers.application.order;


import com.loopers.domain.order.OrderProduct;

import java.util.List;

public record OrderCommand(
        String userId,
        List<OrderProduct> orderProducts,
		Long couponId
) {

}
