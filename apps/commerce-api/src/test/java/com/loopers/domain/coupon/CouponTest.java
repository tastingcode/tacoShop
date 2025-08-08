package com.loopers.domain.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

	/**
	 * 쿠폰 단위 테스트
	 * -[x] 쿠폰 코드가 null 이면 쿠폰 생성 시 예외가 발생한다.
	 * -[x] 쿠폰 타입이 null 이면 쿠폰 생성 시 예외가 발생한다.
	 * -[x] 최소 주문 금액이 0보다 작으면 쿠폰 생성 시 예외가 발생한다.
	 * -[x] 올바른 쿠폰정보를 입력하여 쿠폰을 생성할 수 있다.
	 * -[x] 주문 금색이 최소 가격 이상이어야 한다.
	 * -[x] 정액 쿠폰 할인 금액을 계산할 수 있다.
	 * -[x] 정률 쿠폰 할인 금액을 계산할 수 있다.
	 */

	private final String COUPON_NAME = "couponName";
	private final String COUPON_CODE = "couponCode";
	private final int _10000_ORDER_PRICE = 10000;
	private final int _3000_DISCOUNT_FIXED_AMOUNT = 3000;
	private final int _10_DISCOUNT_PERCENTAGE = 10;

	@DisplayName("쿠폰 코드가 null 이면 쿠폰 생성 시 예외가 발생한다.")
	@Test
	void returnsIllArgException_whenCouponCodeIsNull() {
		// aaa
		assertThatThrownBy(() -> Coupon.builder()
				.name(COUPON_NAME)
				.couponCode(null)
				.type(CouponType.FIXED_AMOUNT)
				.minimumOrderPrice(_10000_ORDER_PRICE)
				.discountFixedAmount(_3000_DISCOUNT_FIXED_AMOUNT)
				.build())
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("쿠폰 타입이 null 이면 쿠폰 생성 시 예외가 발생한다.")
	@Test
	void returnsIllArgException_whenCouponTypeIsNull() {
		// aaa
		assertThatThrownBy(() -> Coupon.builder()
				.name(COUPON_NAME)
				.couponCode(COUPON_CODE)
				.type(null)
				.minimumOrderPrice(_10000_ORDER_PRICE)
				.discountFixedAmount(_3000_DISCOUNT_FIXED_AMOUNT)
				.build())
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("최소 주문 금액이 0보다 작으면 쿠폰 생성 시 예외가 발생한다.")
	@Test
	void returnsIllArgException_whenOrderPriceIsLessThanZero() {
		// aaa
		assertThatThrownBy(() -> Coupon.builder()
				.name(COUPON_NAME)
				.couponCode(COUPON_CODE)
				.type(null)
				.minimumOrderPrice(-1)
				.discountFixedAmount(_3000_DISCOUNT_FIXED_AMOUNT)
				.build())
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("올바른 쿠폰정보를 입력하여 쿠폰을 생성할 수 있다.")
	@Test
	void success_createCoupon() {
		// arrange && act
		Coupon coupon = Coupon.builder()
				.name(COUPON_NAME)
				.couponCode(COUPON_CODE)
				.type(CouponType.FIXED_AMOUNT)
				.discountFixedAmount(_3000_DISCOUNT_FIXED_AMOUNT)
				.build();

		// assert
		assertAll(
				() -> assertThat(coupon.getId()).isNotNull(),
				() -> assertThat(coupon.getName()).isEqualTo(COUPON_NAME),
				() -> assertThat(coupon.getCouponCode()).isEqualTo(COUPON_CODE)
		);
	}

	@DisplayName("정액 쿠폰 할인 금액을 계산할 수 있다.")
	@Test
	void calcDiscountFixedAmount() {
		// arrange
		Coupon coupon = Coupon.builder()
				.name(COUPON_NAME)
				.couponCode(COUPON_CODE)
				.type(CouponType.FIXED_AMOUNT)
				.minimumOrderPrice(_10000_ORDER_PRICE)
				.discountFixedAmount(_3000_DISCOUNT_FIXED_AMOUNT)
				.build();

		final int _20000_ORDER_PRICE = 20000;

		// act
		int discountAmount = coupon.calcDiscountAmount(_20000_ORDER_PRICE);

		// assert
		assertThat(discountAmount).isEqualTo(_3000_DISCOUNT_FIXED_AMOUNT);

	}

	@DisplayName("정률 쿠폰 할인 금액을 계산할 수 있다.")
	@Test
	void calcDiscountPercentage() {
		// arrange
		Coupon coupon = Coupon.builder()
				.name(COUPON_NAME)
				.couponCode(COUPON_CODE)
				.type(CouponType.PERCENTAGE)
				.minimumOrderPrice(_10000_ORDER_PRICE)
				.discountPercentage(_10_DISCOUNT_PERCENTAGE)
				.build();

		final int _20000_ORDER_PRICE = 20000;
		int discountAmount = (int) (_20000_ORDER_PRICE * (_10_DISCOUNT_PERCENTAGE / 100.0));

		// act
		int calcDiscountAmount = coupon.calcDiscountAmount(_20000_ORDER_PRICE);

		// assert
		assertThat(calcDiscountAmount).isEqualTo(discountAmount);

	}

}
