package com.loopers.application.user;

import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.constant.Gender;

public record UserInfo(Long id, String userId, String name, Gender gender, String birth, String email) {
	public static UserInfo from(UserEntity user){
		return new UserInfo(
				user.getId(),
				user.getUserId(),
				user.getName(),
				user.getGender(),
				user.getBirth(),
				user.getEmail()
		);
	}
}
