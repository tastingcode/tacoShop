package com.loopers.interfaces.api.user;

import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.constant.Gender;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserV1ApiE2ETest {

	private final TestRestTemplate testRestTemplate;
    private final UserJpaRepository userJpaRepository;
	private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public UserV1ApiE2ETest(TestRestTemplate testRestTemplate,UserJpaRepository userJpaRepository, DatabaseCleanUp databaseCleanUp) {
        this.testRestTemplate = testRestTemplate;
        this.userJpaRepository = userJpaRepository;
        this.databaseCleanUp = databaseCleanUp;
    }

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

    /**
     * 회원가입 E2E 테스트
     * - [x]  회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다.
     * - [x]  회원 가입 시에 성별이 없을 경우, `400 Bad Request` 응답을 반환한다.
     */

	@DisplayName("POST /api/v1/users")
	@Nested
	class Join {
		public static final String ENDPOINT = "/api/v1/users";

		@DisplayName("회원 가입이 성공할 경우, 생성된 유저 정보를 반환한다.")
		@Test
		void returnsUserInfo_whenJoinIsSuccessful() {
			// arrange
			UserV1Dto.SignUpRequest signUpRequest = new UserV1Dto.SignUpRequest(
					"tempUser",
					"량호",
					Gender.M,
					"1234-12-34",
					"tempUser@gmail.com"
			);

			// act
			ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType =
					new ParameterizedTypeReference<>() {};
			ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
					testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(signUpRequest), responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
					() -> assertThat(response.getBody()).isNotNull(),
					() -> assertThat(response.getBody().data().userId()).isEqualTo(signUpRequest.userId()),
					() -> assertThat(response.getBody().data().name()).isEqualTo(signUpRequest.name())
			);
		}

		@DisplayName("회원 가입 시에 성별이 없을 경우, 400 Bad Request를 반환한다.")
		@Test
		void returnsBadRequest_whenGenderIsMissing() {
			// arrange
			UserV1Dto.SignUpRequest signUpRequest = new UserV1Dto.SignUpRequest(
					"tempUser",
					"량호",
					null,
					"1234-12-34",
					"tempUser@gmail.com"
			);

			// act
			ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType =
					new ParameterizedTypeReference<>() {};

			ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
					testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(signUpRequest), responseType);

			// assert
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		}
	}

    /**
     * 내 정보 조회
     * - [x]  내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다.
     * - [x]  존재하지 않는 ID 로 조회할 경우, `404 Not Found` 응답을 반환한다.
     */
    @DisplayName("GET /api/v1/users/{id}")
    @Nested
    class Find {
        private static final Function<String, String> ENDPOINT_GET = id -> "/api/v1/users/" + id;

        @DisplayName("내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다.")
        @Test
        void returnsTargetUserInfo_whenFindIsSuccessful() {
            // arrange
            UserEntity savedUser = userJpaRepository.save(
                    new UserEntity("tempUser", "량호", Gender.M, "tempUser@gmail.com", "2020-12-12")
            );

            String endpoint = ENDPOINT_GET.apply(savedUser.getUserId());
            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                    testRestTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<>(null), responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getBody().data().userId()).isEqualTo(savedUser.getUserId()),
                    () -> assertThat(response.getBody().data().email()).isEqualTo(savedUser.getEmail())
            );

        }
        
        @DisplayName("존재하지 않는 ID 로 조회할 경우, 404 Not Found 응답을 반환한다.")
        @Test
        void throwsBadRequest_whenInvalidIdIsProvided() {
            // arrange
            String invalidUsedId = "emptyUserId";
            String endpoint = ENDPOINT_GET.apply(invalidUsedId);

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                    testRestTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<>(null), responseType);
            
            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 포인트 조회
     * - [x]  포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.
     * - [x]  `X-USER-ID` 헤더가 없을 경우, `400 Bad Request` 응답을 반환한다.
     */
    @DisplayName("GET /api/v1/users/points")
    @Nested
    class FindPoint{
        public static final String ENDPOINT = "/api/v1/users/points";

        @DisplayName("포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.")
        @Test
        void returnsPoint_whenFindIsSuccessful() {
            // arrange
            UserEntity savedUser = userJpaRepository.save(
                    new UserEntity("tempUser", "량호", Gender.M, "tempUser@gmail.com", "2020-12-12")
            );
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", "tempUser");

            // act
            ParameterizedTypeReference<ApiResponse<Long>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<Long>> response =
                    testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(null, headers), responseType);

            // assert
            assertThat(response.getBody().data()).isEqualTo(savedUser.getPoint());

        }

        @DisplayName("X-USER-ID 헤더가 없을 경우, 400 Bad Request 응답을 반환한다.")
        @Test
        void returnsBadRequest_whenXUserIdHeaderIsMissing() {
            // arrange
            HttpHeaders headers = new HttpHeaders();

            // act
            ParameterizedTypeReference<ApiResponse<Long>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<Long>> response =
                    testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(null, headers), responseType);

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

	/**
	 * 포인트 충전
	 * - [x]  존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다.
	 * - [x]  존재하지 않는 유저로 요청할 경우, `404 Not Found` 응답을 반환한다.
	 */
	@DisplayName("POST /api/v1/users/points")
	@Nested
	class ChargePoint{
		public static final String ENDPOINT = "/api/v1/users/points";

		@DisplayName("존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다.")
		@Test
		void returnsPoint_whenMemberChargePointSuccessfully() {
		    // arrange
			UserEntity savedUser = userJpaRepository.save(
					new UserEntity("tempUser", "량호", Gender.M, "tempUser@gmail.com", "2020-12-12")
			);
			UserV1Dto.PointRequest pointRequest = new UserV1Dto.PointRequest(1000L);
			long totalPoint = savedUser.getPoint() + pointRequest.amount();

			HttpHeaders headers = new HttpHeaders();
			headers.set("X-USER-ID", "tempUser");

		    // act
			ParameterizedTypeReference<ApiResponse<UserV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {};
			ResponseEntity<ApiResponse<UserV1Dto.PointResponse>> response = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(pointRequest, headers), responseType);

			// assert
			assertThat(response.getBody().data().totalAmount()).isEqualTo(totalPoint);
		}


		@DisplayName("존재하지 않는 유저로 요청할 경우, `404 Not Found` 응답을 반환한다.")
		@Test
		void returnsNotFound_whenNotUserChargePoint() {
		    // arrange
			UserV1Dto.PointRequest pointRequest = new UserV1Dto.PointRequest(1000L);

			HttpHeaders headers = new HttpHeaders();
			headers.set("X-USER-ID", "NotUser");

		    // act
			ParameterizedTypeReference<ApiResponse<UserV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {};
			ResponseEntity<ApiResponse<UserV1Dto.PointResponse>> response = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(pointRequest, headers), responseType);

		    // assert
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		}
	}



}
