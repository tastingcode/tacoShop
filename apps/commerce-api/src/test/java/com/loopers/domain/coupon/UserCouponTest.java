package com.loopers.domain.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserCouponTest {

	/**
	 * -[x] 사용자 아이디가 null 이면 유저 쿠폰 생성 시 예외가 발생한다.
	 * -[x] 쿠폰 아이디가 null 이면 유저 쿠폰 생성 시 예외가 발생한다.
	 * -[x] 올바른 유저 쿠폰 정보를 입력하여 유저쿠폰을 생성할 수 있다.
	 * -[x] 사용되지 않은 쿠폰을 사용할 수 있다.
	 * -[x] 이미 사용된 쿠폰을 사용하면 예외가 발생한다.
	 */


	@DisplayName("사용자 아이디가 null 이면 유저 쿠폰 생성 시 예외가 발생한다.")
	@Test
	void returnsIllArgException_whenUserIdIsNull() {
	    // arrange
		Long userId = null;
		Long couponId = 1L;

	    // act && assert
		assertThrows(IllegalArgumentException.class, () -> UserCoupon.of(userId, couponId));

	}

	@DisplayName("쿠폰 아이디가 null 이면 유저 쿠폰 생성 시 예외가 발생한다.")
	@Test
	void returnsIllArgException_whenCouponIdIsNull() {
		// arrange
		Long userId = 1L;
		Long couponId = null;

		// act && assert
		assertThrows(IllegalArgumentException.class, () -> UserCoupon.of(userId, couponId));

	}

	@DisplayName("올바른 유저 쿠폰 정보를 입력하여 유저쿠폰을 생성할 수 있다.")
	@Test
	void success_ofUserCoupon() {
	    // arrange
		Long userId = 1L;
		Long couponId = 1L;

	    // act
		UserCoupon userCoupon = UserCoupon.of(userId, couponId);

	    // assert
		assertThat(userCoupon.getUserId()).isEqualTo(userId);
		assertThat(userCoupon.isUsed()).isEqualTo(false);

	}

	@DisplayName("사용되지 않은 쿠폰을 사용할 수 있다.")
	@Test
	void success_useCoupon() {
	    // arrange
		Long userId = 1L;
		Long couponId = 1L;
		UserCoupon userCoupon = UserCoupon.of(userId, couponId);

	    // act
		userCoupon.useCoupon();

	    // assert
		assertThat(userCoupon.isUsed()).isEqualTo(true);

	}

	@DisplayName("이미 사용된 쿠폰을 사용하면 예외가 발생한다.")
	@Test
	void returns_IllStaExceptionWhenUseCoupon() {
		// arrange
		Long userId = 1L;
		Long couponId = 1L;
		UserCoupon userCoupon = UserCoupon.of(userId, couponId);
		userCoupon.useCoupon();

		// act && assert
		assertThrows(IllegalStateException.class, () -> userCoupon.useCoupon());

	}


}
