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
public class UserDomainService {
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

}
