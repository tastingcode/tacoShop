package com.loopers.interfaces.api.point;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRepository;
import com.loopers.domain.user.constant.Gender;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PointV1ApiE2ETest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PointRepository pointRepository;
	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	/**
	 * 포인트 조회
	 * - [x]  포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.
	 * - [x]  `X-USER-ID` 헤더가 없을 경우, `400 Bad Request` 응답을 반환한다.
	 */
	@DisplayName("GET /api/v1/points")
	@Nested
	class FindPoint{
		public static final String ENDPOINT = "/api/v1/points";

		@DisplayName("포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.")
		@Test
		void returnsPoint_whenFindIsSuccessful() {
			// arrange
			UserEntity savedUser = userRepository.save(
					new UserEntity("tempUser", "량호", Gender.M, "tempUser@gmail.com", "2020-12-12")
			);
			int chargePoint = 1000;
			Point savedPoint = pointRepository.save(Point.of(savedUser.getId(), chargePoint));

			HttpHeaders headers = new HttpHeaders();
			headers.set("X-USER-ID", "tempUser");

			// act
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {};
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response =
					testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(null, headers), responseType);

			// assert
			assertThat(response.getBody().data().amount()).isEqualTo(savedPoint.getAmount());

		}

		@DisplayName("X-USER-ID 헤더가 없을 경우, 400 Bad Request 응답을 반환한다.")
		@Test
		void returnsBadRequest_whenXUserIdHeaderIsMissing() {
			// arrange
			HttpHeaders headers = new HttpHeaders();

			// act
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {};
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response =
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
	@DisplayName("POST /api/v1/points/charge")
	@Nested
	class ChargePoint{
		public static final String ENDPOINT = "/api/v1/points/charge";

		@DisplayName("존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다.")
		@Test
		void returnsPoint_whenMemberChargePointSuccessfully() {
			// arrange
			UserEntity savedUser = userRepository.save(
					new UserEntity("tempUser", "량호", Gender.M, "tempUser@gmail.com", "2020-12-12")
			);
			int chargePoint = 1000;

			PointV1Dto.PointRequest pointRequest = new PointV1Dto.PointRequest(chargePoint);

			HttpHeaders headers = new HttpHeaders();
			headers.set("X-USER-ID", "tempUser");

			// act
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {};
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(pointRequest, headers), responseType);

			// assert
			assertThat(response.getBody().data().amount()).isEqualTo(chargePoint);
		}


		@DisplayName("존재하지 않는 유저로 요청할 경우, `404 Not Found` 응답을 반환한다.")
		@Test
		void returnsNotFound_whenNotUserChargePoint() {
			// arrange
			int chargePoint = 1000;

			PointV1Dto.PointRequest pointRequest = new PointV1Dto.PointRequest(chargePoint);

			HttpHeaders headers = new HttpHeaders();
			headers.set("X-USER-ID", "NotUser");

			// act
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {};
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(pointRequest, headers), responseType);

			// assert
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		}
	}

}
