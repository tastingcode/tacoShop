package com.loopers.application.coupon.event;

import com.loopers.application.coupon.CouponService;
import com.loopers.domain.order.OrderDomainService;
import com.loopers.domain.order.event.OrderCreatedEvent;
import com.loopers.domain.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponEventHandler {

	private final CouponService couponService;
	private final UserDomainService userDomainService;
	private final OrderDomainService orderDomainService;

	// 쿠폰 사용
	public void useCoupon(OrderCreatedEvent event) {
		Long userId = event.userId();
		Long couponId = event.couponId();
		couponService.useCoupon(userId, couponId);
	}

	// 쿠폰 복구
	public void restoreCoupon(String userId, Long orderId) {
		Long id = userDomainService.getUserByUserId(userId).getId();
		Long couponId = orderDomainService.getOrder(orderId).getCouponId();
		couponService.restoreCoupon(id, couponId);
	}

}
