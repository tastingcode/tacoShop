package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserV1ApiController implements UserV1ApiSpec {

	private final UserFacade userFacade;

	@PostMapping
	@Override
	public ApiResponse<UserV1Dto.UserResponse> signUp(
			@RequestBody @Valid UserV1Dto.SignUpRequest signUpRequest) {

		UserInfo user = userFacade.createUser(signUpRequest);
		UserV1Dto.UserResponse response = UserV1Dto.UserResponse.from(user);
		return ApiResponse.success(response);

	}

}
