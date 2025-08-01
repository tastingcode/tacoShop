package com.loopers.domain.user;

import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserInfo createUser(UserV1Dto.SignUpRequest signUpRequest) {
        UserEntity userEntity = new UserEntity(signUpRequest.userId(),
                signUpRequest.name(),
                signUpRequest.gender(),
                signUpRequest.email(),
                signUpRequest.birth());

        UserEntity user
                = userRepository.createUser(userEntity)
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "회원 가입에 실패하였습니다."));

        return UserInfo.from(user);
    }

    @Transactional(readOnly = true)
    public UserEntity getUser(String userId) {
        return userRepository.findByUserId(userId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Long getUserPoint(String userId) {
        UserEntity user = userRepository.findByUserId(userId).orElse(null);
        return user != null ? user.getPoint() : null;
    }

	@Transactional
	public Long chargePoint(String userId, UserV1Dto.PointRequest pointRequest) {
		UserEntity user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다."));

		user.addPoint(pointRequest.amount());
		return user.getPoint();
	}

}
