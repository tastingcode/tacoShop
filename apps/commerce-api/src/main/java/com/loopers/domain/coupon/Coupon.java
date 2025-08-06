package com.loopers.domain.coupon;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String couponCode;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CouponType type;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CouponStatus status = CouponStatus.ACTIVE;
	private int minimumOrderPrice;
	private int discountFixedAmount;
	private int discountPercentage;

	@Builder
	public Coupon(String name,
				  String couponCode,
				  CouponType type,
				  int minimumOrderPrice,
				  int discountFixedAmount,
				  int discountPercentage) {
		if (name == null || couponCode == null || type == null || minimumOrderPrice < 0) {
			throw new IllegalArgumentException("쿠폰 등록 정보가 올바르지 않습니다.");
		}
		this.name = name;
		this.couponCode = couponCode;
		this.type = type;
		this.minimumOrderPrice = minimumOrderPrice;
		this.discountFixedAmount = discountFixedAmount;
		this.discountPercentage = discountPercentage;
	}

	public int calcDiscountAmount(int orderPrice) {
		int discountAmount = 0;
		if (type == CouponType.FIXED_AMOUNT)
			discountAmount = discountFixedAmount;
		if (type == CouponType.PERCENTAGE)
			discountAmount = (int) (orderPrice * (discountPercentage / 100.0));

		return discountAmount;
	}

	public boolean isUsable(int orderPrice) {
		boolean usable = false;
		if (minimumOrderPrice <= orderPrice)
			usable = true;

		return usable;
	}

	public void updateStatus(CouponStatus status) {
		this.status = status;
	}
}
