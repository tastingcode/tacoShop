package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserInfo;
import com.loopers.domain.user.constant.Gender;
import jakarta.validation.constraints.NotNull;

public class UserV1Dto {
	public record SignUpRequest(
			@NotNull
			String userId,
			@NotNull
			String name,
			@NotNull
			Gender gender,
			@NotNull
			String birth,
			@NotNull
			String email
	) { }

	public record UserResponse(
			String userId,
			String name,
			Gender gender,
			String birth,
			String email
	) {
		public static UserResponse from(UserInfo userInfo) {
			return new UserResponse(
					userInfo.userId(),
					userInfo.name(),
					userInfo.gender(),
					userInfo.birth(),
					userInfo.email()
			);
		}
	}

	public record PointRequest(Long amount){ }

	public record PointResponse(Long totalAmount){
		public static PointResponse from(Long totalAmount) {
			return new PointResponse(totalAmount);
		}
	}
}
