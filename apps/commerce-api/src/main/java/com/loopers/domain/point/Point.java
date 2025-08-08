package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseEntity {
	@Column(name = "ref_user_id", nullable = false)
	private Long userId;
	private int amount;

	public static Point of(Long userId, int amount) {
		if (userId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "사용자 ID는 비어있을 수 없습니다.");
		}
		if (amount < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "포인트는 0보다 작을 수 없습니다.");
		}
		Point point = new Point();
		point.userId = userId;
		point.amount = amount;
		return point;
	}

	public int chargePoint(int point) {
		if (point <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "충전 포인트는 0보다 커야합니다.");
		}
		this.amount += point;
		return this.amount;
	}

	public int usePoint(int point) {
		if (point < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "사용 할 포인트는 0보다 작을 수 없습니다.");
		}
		if (this.amount < point) {
			throw new CoreException(ErrorType.BAD_REQUEST, "잔여 포인트가 부족합니다.");
		}
		this.amount -= point;
		return this.amount;
	}


}
