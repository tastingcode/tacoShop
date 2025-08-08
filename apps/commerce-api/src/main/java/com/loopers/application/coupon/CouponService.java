package com.loopers.application.coupon;

import com.loopers.domain.coupon.*;
import com.loopers.domain.user.UserEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponRepository couponRepository;
	private final UserCouponRepository userCouponRepository;
	private final CouponDomainService couponDomainService;

	public int calcDiscountAmount(Long userId, Long couponId, int orderPrice) {
		// 유효 쿠폰 조회
		Coupon coupon = getCouponById(couponId);

		// 유저 쿠폰 조회
		UserCoupon userCoupon = getUserCouponByUserIdAndCouponId(userId, couponId);

		int discountAmount = 0;
		if (couponDomainService.isUsableCoupon(coupon, userCoupon, orderPrice))
			discountAmount = coupon.calcDiscountAmount(orderPrice);

		return discountAmount;
	}

	@Transactional
	public void useCoupon(Long userId, Long couponId) {
		Coupon coupon = getCouponById(couponId);
		UserCoupon userCoupon = getUserCouponByUserIdAndCouponId(userId, couponId);

		couponDomainService.useCoupon(coupon, userCoupon);
	}

	public Coupon getCouponById(Long couponId) {
		return couponRepository.findByCouponId(couponId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "유효하지 않은 쿠폰입니다."));
	}

	public UserCoupon getUserCouponByUserIdAndCouponId(Long userId , Long couponId) {
		return userCouponRepository.findByUserIdAndCouponId(userId, couponId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "유저 쿠폰이 존재하지 않습니다."));
	}


	public UserCoupon createUserCoupon(UserEntity user, Coupon coupon) {
		UserCoupon userCoupon = UserCoupon.of(user.getId(), coupon.getId());
		coupon.updateStatus(CouponStatus.ISSUED);
		return userCoupon;
	}

}
