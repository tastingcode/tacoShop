package com.loopers.application.user;

import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserDomainService;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserDomainService userDomainService;

	public UserInfo createUser(UserV1Dto.SignUpRequest signUpRequest) {
        return userDomainService.createUser(signUpRequest);
	}

    public UserInfo getUserInfo(String userId) {
        UserEntity user = userDomainService.getUser(userId);
        if (user == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
        return UserInfo.from(user);
    }

}
