package com.loopers.domain.coupon;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends BaseEntity {

	@Column(name = "ref_user_id", nullable = false)
	private Long userId;
	@Column(name = "ref_coupon_id", nullable = false)
	private Long couponId;
	private boolean used = false;
	@Version
	private Long version;

	public static UserCoupon of(Long userId, Long couponId) {
		UserCoupon userCoupon = new UserCoupon();
		if (userId == null || couponId == null) {
			throw new IllegalArgumentException("유저 쿠폰 정보가 올바르지 않습니다.");
		}
		userCoupon.userId = userId;
		userCoupon.couponId = couponId;
		return userCoupon;
	}

	public void useCoupon() {
		if (this.used) {
			throw new IllegalStateException("이미 사용된 쿠폰입니다.");
		}
		this.used = true;
	}

}
