package com.loopers.application.point;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRepository;
import com.loopers.domain.user.constant.Gender;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PointServiceIntgTest {

	@Autowired
	private PointService pointService;

	@Autowired
	private PointRepository pointRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	/**
	 * 포인트 조회
	 * - [x]  해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.
	 * - [x]  해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.
	 */
	@DisplayName("포인트 조회")
	@Nested
	class FindPoint {
		@DisplayName("해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.")
		@Test
		void returnsPoint_whenUserExists() {
			// arrange
			UserEntity savedUser = userRepository.save(
					new UserEntity("tempUser", "량호", Gender.M, "tempUser@gmail.com", "2020-12-12")
			);
			int chargePoint = 1000;
			Point savedPoint = pointRepository.save(Point.of(savedUser.getId(), chargePoint));

			// act
			PointInfo pointInfo = pointService.getUserPoint(savedUser.getUserId());


			// assert
			assertThat(pointInfo.amount()).isEqualTo(chargePoint);
		}

		@DisplayName("해당 ID 의 회원이 존재하지 않을 경우, 예외가 발생한다.")
		@Test
		void returnsNull_whenUserDoesNotExist() {
			// arrange
			String invalidUserId = "invalidUserId";

			// act && assert
			assertThrows(CoreException.class, () -> pointService.getUserPoint(invalidUserId));

		}
	}

	/**
	 * 포인트 충전
	 * - [x] 존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다.
	 */
	@DisplayName("포인트 충전 테스트")
	@Nested
	class ChargePoint {

		@DisplayName("존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다.")
		@Test
		void fail_whenNotUserChargePoint() {
			// arrange
			String notExistUserId = "notExistUserId";

			// act
			CoreException exception = assertThrows(CoreException.class, () -> {
				pointService.chargePoint(notExistUserId, 30000);
			});

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);

		}

	}

	@DisplayName("동일한 유저가 서로 다른 주문을 동시에 수행해도, 포인트가 정상적으로 차감되어야 한다.")
	@Test
	void 동일한_유저가_서로_다른_주문을_동시에_수행해도_포인트가_정상적으로_차감되어야_한다() throws InterruptedException {
		// arrange
		UserEntity savedUser = userRepository.save(
				new UserEntity("tempUser", "량호", Gender.M, "tempUser@gmail.com", "2020-12-12")
		);
		int chargePoint = 100000;
		Point savedPoint = pointRepository.save(Point.of(savedUser.getId(), chargePoint));

		int useAmount = 5000;

		int threadCount = 3;
		CountDownLatch latch = new CountDownLatch(threadCount);
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		AtomicInteger successCount = new AtomicInteger(0);

		// act
		for (int i = 1; i <= threadCount; i++) {
			executorService.submit(() -> {
				try {
					pointService.useMyPoint(savedUser.getUserId(), useAmount);
					successCount.incrementAndGet();
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();
		executorService.shutdown();

		Point foundPoint = transactionTemplate.execute(status ->
				pointRepository.findByUserIdForUpdate(savedUser.getId()).get()
		);

		// assert
		assertThat(successCount.get()).isEqualTo(threadCount);
		assertThat(foundPoint.getAmount()).isEqualTo(chargePoint - (useAmount * threadCount));
	}


}
