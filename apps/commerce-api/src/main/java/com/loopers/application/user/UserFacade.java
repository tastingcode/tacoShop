package com.loopers.application.user;

import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.user.UserV1Dto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {
	private final UserService userService;

	public UserInfo createUser(UserV1Dto.SignUpRequest signUpRequest) {
		return userService.createUser(signUpRequest);
	}
}
