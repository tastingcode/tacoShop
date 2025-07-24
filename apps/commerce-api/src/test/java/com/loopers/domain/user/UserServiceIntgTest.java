package com.loopers.domain.user;

import com.loopers.application.user.UserInfo;
import com.loopers.domain.user.constant.Gender;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserServiceIntgTest {
	/**
	 * 회원 가입 통합 테스트
	 * - [x]  회원 가입시 User 저장이 수행된다. ( spy 검증 )
	 * - [x]  이미 가입된 ID 로 회원가입 시도 시, 실패한다.
	 */
	@Autowired
	private UserService userService;

	@MockitoSpyBean
	private UserRepository userRepository;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	@DisplayName("회원 가입 통합 테스트")
	@Nested
	class Join{
		@DisplayName("회원 가입시 User 저장이 수행된다. (spy 검증)")
		@Test
		void isUserSaveActionWhenUserSignUp() {
			// arrange
			final String userId = "tempUser";
			final String name = "량호";
			final Gender gender = Gender.M;
			final String birth = "2020-12-12";
			final String email = "temp@gmail.com";

			UserV1Dto.SignUpRequest signUpRequest = new UserV1Dto.SignUpRequest(
					userId,
					name,
					gender,
					birth,
					email
			);

			ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);

			// act
			UserInfo user = userService.createUser(signUpRequest);
			UserV1Dto.UserResponse userResponse = UserV1Dto.UserResponse.from(user);

			// assert
			assertAll(
					() -> assertThat(userResponse.userId()).isEqualTo(userId),
					() -> assertThat(userResponse.email()).isEqualTo(email)
			);

			verify(userRepository, times(1)).createUser(userEntityArgumentCaptor.capture());
			UserEntity capturedUser = userEntityArgumentCaptor.getValue();
			assertThat(capturedUser.getUserId()).isEqualTo(userId);
			assertThat(capturedUser.getEmail()).isEqualTo(email);
		}

		@DisplayName("이미 가입된 ID 로 회원가입 시도 시, 실패한다.")
		@Test
		void failTestWhenJoinByIdAlreadyJoined() {
		    // arrange
			final String userId1 = "tempUser";
			final String userId2 = "tempUser";
			final String name = "량호";
			final Gender gender = Gender.M;
			final String birth = "2020-12-12";
			final String email = "temp@gmail.com";

			UserV1Dto.SignUpRequest signUpRequest1 = new UserV1Dto.SignUpRequest(
					userId1,
					name,
					gender,
					birth,
					email
			);

			UserV1Dto.SignUpRequest signUpRequest2 = new UserV1Dto.SignUpRequest(
					userId2,
					name,
					gender,
					birth,
					email
			);

			// act
			userService.createUser(signUpRequest1);

		    // assert
			assertThrows(CoreException.class, () -> userService.createUser(signUpRequest2));
		}
	}

    /**
     * 내 정보 조회
     * - [x]  해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다.
     * - [x]  해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.
     */
    @DisplayName("GET /api/v1/users/{id}")
    @Nested
    class Find {
        private static final Function<String, String> ENDPOINT_GET = id -> "/api/v1/users/" + id;

        @DisplayName("해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다.")
        @Test
        void returnsTargetUserInfo_whenFindIsSuccessful() {
            // arrange
            String userId = "tempUser";
            String email = "tempUser@gmail.com";
            UserEntity savedUser = userJpaRepository.save(
                    new UserEntity(userId, "량호", Gender.M, email, "2020-12-12")
            );

            // act
            UserEntity user = userService.getUser(savedUser.getUserId());

            // assert
            assertAll(
                    () -> assertThat(user).isNotNull(),
                    () -> assertThat(user.getUserId()).isEqualTo(userId),
                    () -> assertThat(user.getEmail()).isEqualTo(email)
            );
        }

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.")
        @Test
        void returnsNull_whenInvalidIdIsProvided() {
            // arrange
            String invalidUsedId = "emptyUserId";

            // act
            UserEntity user = userService.getUser(invalidUsedId);


            // assert
            assertThat(user).isNull();
        }
    }

    /**
     * 포인트 조회
     * - [x]  해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.
     * - [x]  해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.
     */
    @DisplayName("GET /api/v1/users/points")
    @Nested
    class FindPoint {
        public static final String ENDPOINT = "/api/v1/users/points";

        @DisplayName("해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.")
        @Test
        void returnsPoint_whenUserExists() {
            // arrange
            String userId = "tempUser";
            String email = "tempUser@gmail.com";
            UserEntity savedUser = userJpaRepository.save(
                    new UserEntity(userId, "량호", Gender.M, email, "2020-12-12")
            );

            // act
            Long point = userService.getUserPoint(userId);

            // assert
            assertThat(point).isEqualTo(savedUser.getPoint());
        }

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.")
        @Test
        void returnsNull_whenUserDoesNotExist() {
            // arrange
            String invalidUserId = "emptyUserId";

            // act
            Long point = userService.getUserPoint(invalidUserId);

            // assert
            assertThat(point).isNull();
        }
    }

	/**
	 * 포인트 충전
	 * - [x] 존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다.
	 */
	@DisplayName("POST /api/v1/users/points")
	@Nested
	class ChargePoint{

		@DisplayName("존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다.")
		@Test
		void fail_whenNotUserChargePoint() {
			// arrange
			UserV1Dto.PointRequest pointRequest = new UserV1Dto.PointRequest(1000L);
			String notUserId = "notUser";

		    // act
			CoreException exception = assertThrows(CoreException.class, () -> {
				userService.chargePoint(notUserId, pointRequest);
			});

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);

		}

	}

}
