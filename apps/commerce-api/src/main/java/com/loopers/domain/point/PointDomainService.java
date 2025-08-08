package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class PointDomainService {

	public Point chargePoint(Point point, int chargeAmount) {
		if (chargeAmount <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "충전 포인트는 0보다 커야합니다.");
		}

		point.chargePoint(chargeAmount);
		return point;
	}

	public Point usePoint(Point point, int useAmount) {
		if (useAmount <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "사용할 포인트는 0보다 커야합니다.");
		}

		point.usePoint(useAmount);
		return point;
	}
}
