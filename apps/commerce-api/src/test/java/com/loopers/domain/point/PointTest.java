package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PointTest {



	/**
	 * 포인트 충전 단위 테스트
	 * - [x] 0 이하의 정수로 포인트를 충전 시 실패한다
	 */
	@DisplayName("0 이하의 정수로 포인트를 충전 시 실패한다")
	@ParameterizedTest
	@ValueSource(ints = {0,-1000})
	void fail_whenAddingPointAmountIsZeroOrNegative(int amount) {
		// arrange
		Point point = Point.of(1L, 1000);

		// act
		CoreException exception = assertThrows(CoreException.class, () -> {
			point.chargePoint(amount);
		});

		// assert
		assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

	}

}
