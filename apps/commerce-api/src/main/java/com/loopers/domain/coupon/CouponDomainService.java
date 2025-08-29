package com.loopers.domain.coupon;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CouponDomainService {

	public boolean isUsableCoupon(Coupon coupon, UserCoupon userCoupon, int orderPrice){
		boolean usable = false;
		if (coupon.isUsable(orderPrice) && !userCoupon.isUsed())
			usable = true;

		return usable;
	}

	public void useCoupon(Coupon coupon, UserCoupon userCoupon) {
		coupon.updateStatus(CouponStatus.USED);
		userCoupon.useCoupon();
	}

	public void restoreCoupon(Coupon coupon, UserCoupon userCoupon) {
		coupon.updateStatus(CouponStatus.ACTIVE);
		userCoupon.restoreCoupon();
	}
}

