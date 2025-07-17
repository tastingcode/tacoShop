package com.loopers.interfaces.api.user;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "User V1 API", description = "사용자 API 입니다.")
public interface UserV1ApiSpec {

    @Operation(summary = "회원 가입")
    ApiResponse<UserV1Dto.UserResponse> signUp(
            UserV1Dto.SignUpRequest signUpRequest
    );

    @Operation(summary = "유저 정보 조회", description = "유저 정보를 조회합니다.")
    ApiResponse<UserV1Dto.UserResponse> getUserInfo(
            @Schema(name = "ID")
            String userId
    );

    @Operation(summary = "포인트 조회")
    ApiResponse<Long> getUserPoint(
            @Schema(name = "X-USER-ID")
            @RequestHeader(name = "X-USER-ID") String headerUserId
    );


}
