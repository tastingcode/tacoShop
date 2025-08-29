package com.loopers.application.order;

import java.util.List;

public record OrderCommand(
        String userId,
        List<OrderProductCommand> orderProducts,
		Long couponId
) {

}
