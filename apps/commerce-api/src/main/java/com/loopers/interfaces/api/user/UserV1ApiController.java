package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserV1ApiController implements UserV1ApiSpec {

    private final UserFacade userFacade;

    @PostMapping
    @Override
    public ApiResponse<UserV1Dto.UserResponse> signUp(
            @RequestBody @Valid UserV1Dto.SignUpRequest signUpRequest) {

        UserInfo userInfo = userFacade.createUser(signUpRequest);
        UserV1Dto.UserResponse response = UserV1Dto.UserResponse.from(userInfo);
        return ApiResponse.success(response);

    }

    @GetMapping("/{userId}")
    @Override
    public ApiResponse<UserV1Dto.UserResponse> getUserInfo(
            @PathVariable(value = "userId") String userId
    ) {

        UserInfo userInfo = userFacade.getUserInfo(userId);
        UserV1Dto.UserResponse response = UserV1Dto.UserResponse.from(userInfo);

        return ApiResponse.success(response);
    }

    @GetMapping("/points")
    @Override
    public ApiResponse<Long> getUserPoint(@RequestHeader(name = "X-USER-ID") String headerUserId) {
        Long point = userFacade.getUserPoint(headerUserId);

        return ApiResponse.success(point);
    }

	@PostMapping("/points")
	@Override
	public ApiResponse<UserV1Dto.PointResponse> chargePoint(@RequestHeader (name = "X-USER-ID") String headerUserId,
															UserV1Dto.PointRequest pointRequest) {
		Long totalPoint = userFacade.chargePoint(headerUserId, pointRequest);
		UserV1Dto.PointResponse pointResponse = new UserV1Dto.PointResponse(totalPoint);
		return ApiResponse.success(pointResponse);
	}

}
