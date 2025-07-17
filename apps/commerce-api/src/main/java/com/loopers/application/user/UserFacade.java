package com.loopers.application.user;

import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {
	private final UserService userService;

	public UserInfo createUser(UserV1Dto.SignUpRequest signUpRequest) {
        return userService.createUser(signUpRequest);
	}

    public UserInfo getUserInfo(String userId) {
        UserEntity user = userService.getUser(userId);
        if (user == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
        return UserInfo.from(user);
    }


}
