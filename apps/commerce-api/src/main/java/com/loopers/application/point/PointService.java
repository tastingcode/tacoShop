package com.loopers.application.point;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointDomainService;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

	private final UserRepository userRepository;
	private final PointRepository pointRepository;
	private final PointDomainService pointDomainService;

	@Transactional
	public PointInfo chargePoint(String userId, int amount){
		// 사용자 확인
		UserEntity user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다."));

		// 포인트 조회
		Point point = pointRepository.findByUserIdForUpdate(user.getId())
				.orElseGet(() -> Point.of(user.getId(), 0));

		// 포인트 충전
		Point chargedPoint = pointDomainService.chargePoint(point, amount);
		pointRepository.save(chargedPoint);
		return PointInfo.from(chargedPoint);
	}

	public PointInfo getUserPoint(String userId) {
		// 사용자 확인
		UserEntity user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다."));

		// 포인트 조회
		Point point = pointRepository.findByUserId(user.getId())
				.orElseGet(() -> Point.of(user.getId(), 0));

		return PointInfo.from(point);
	}

	@Transactional
	public PointInfo useMyPoint(String userId, int amount) {
		// 사용자 존재 여부 확인
		UserEntity user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다."));

		// 포인트 조회
		Point point = pointRepository.findByUserIdForUpdate(user.getId())
				.orElseGet(() -> Point.of(user.getId(), 0));

		// 포인트 사용
		Point usedPoint = pointDomainService.usePoint(point, amount);

		// 저장
		Point savedPoint = pointRepository.save(usedPoint);

		return PointInfo.from(savedPoint);
	}

}
