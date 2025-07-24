package com.loopers.domain.user;

import com.loopers.domain.user.constant.Gender;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UserTest {
	/**
	 * 회원가입 단위 테스트
	 * - [x]  ID 가 `영문 및 숫자 10자 이내` 형식에 맞지 않으면, User 객체 생성에 실패한다.
	 * - [x]  이메일이 `xx@yy.zz` 형식에 맞지 않으면, User 객체 생성에 실패한다.
	 * - [x]  생년월일이 `yyyy-MM-dd` 형식에 맞지 않으면, User 객체 생성에 실패한다.
	 */
	@DisplayName("ID 가 영문 및 숫자 10자 이내 형식에 맞지 않으면, User 객체 생성에 실패한다.")
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"aklsdjfnasdlkfjnasdlfjkn12345",
			"한글아이디",
	})
	void fail_whenIdFOrmatisInvalid(String userId) {
		// arrange
		final String name = "량호";
		final Gender gender = Gender.M;
		final String email = "temp@gmail.com";
		final String birth = "2000-12-34";

		// act
		final CoreException exception = assertThrows(CoreException.class, () -> {
			new UserEntity(userId, name, gender, email, birth);
		});

		// assert
		assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
	}

	@DisplayName("이메일이 xx@yy.zz 형식에 맞지 않으면, User 객체 생성에 실패한다.")
	@Test
	void fail_whenEmailFormatIsInvalid() {
		// arrange
		final String userId = "tempUser";
		final String name = "량호";
		final Gender gender = Gender.M;
		final String email = "email";
		final String birth = "2020-12-12";

		// act
		CoreException exception = assertThrows(CoreException.class, () -> {
			new UserEntity(userId, name, gender, email, birth);
		});

		// assert
		assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
	}

	@DisplayName("생년월일이 yyyy-MM-dd 형식에 맞지 않으면, User 객체 생성에 실패한다.")
	@Test
	void fail_whenBirthFormatIsInvalid() {
		// arrange
		final String userId = "tempUser";
		final String name = "량호";
		final Gender gender = Gender.M;
		final String email = "temp@gmail.com";
		final String birth = "2020.12.31";

		// act
		CoreException exception = assertThrows(CoreException.class, () -> {
			new UserEntity(userId, name, gender, email, birth);
		});

		// assert
		assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
	}

	/**
	 * 포인트 충전 단위 테스트
	 * - [x] 0 이하의 정수로 포인트를 충전 시 실패한다
	 */
	@DisplayName("0 이하의 정수로 포인트를 충전 시 실패한다")
	@ParameterizedTest
	@ValueSource(longs = {0,-1000})
	void fail_whenAddingPointAmountIsZeroOrNegative(Long amount) {
	    // arrange
		UserEntity user = new UserEntity("tempUser", "량호", Gender.M, "temp@gmail.com", "2020-12-12");
	    
	    // act
		CoreException exception = assertThrows(CoreException.class, () -> {
			user.addPoint(amount);
		});

		// assert
		assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
	    
	}
}
