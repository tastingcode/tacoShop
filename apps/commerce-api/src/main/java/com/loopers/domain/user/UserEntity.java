package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.user.constant.Gender;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
public class UserEntity extends BaseEntity {

	@Column(name = "user_id", nullable = false, unique = true)
	private String userId;
	@Column(nullable = false)
	private String name;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Column(nullable = false)
	private String birth;
	@Column(nullable = false)
	private String email;

	public static final String PATTERN_USER_ID = "^[a-zA-Z0-9]{1,10}$";
	public static final String PATTERN_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	public static final String PATTERN_BIRTH = "^\\d{4}-\\d{2}-\\d{2}$";

	public UserEntity(
			String userId,
			String name,
			Gender gender,
			String email,
			String birth
	){
		if (userId == null || !userId.matches(PATTERN_USER_ID)) {
			throw new CoreException(ErrorType.BAD_REQUEST, "ID는 영문 및 숫자 10자 이내여야 합니다.");
		}

		if (!email.matches(PATTERN_EMAIL)) {
			throw new CoreException(ErrorType.BAD_REQUEST, "이메일은 xx@yy.zz 형식이여야 합니다.");
		}

		if (!birth.matches(PATTERN_BIRTH)) {
			throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 yyyy-MM-dd 형식에 맞아야 합니다.");
		}

		this.userId = userId;
		this.name = name;
		this.gender = gender;
		this.email = email;
		this.birth = birth;
	}

}
