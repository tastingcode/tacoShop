package com.loopers.application.coupon;

import com.loopers.domain.coupon.*;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRepository;
import com.loopers.domain.user.constant.Gender;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CouponServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private UserCouponRepository userCouponRepository;

	@Autowired
	private CouponService couponService;

	@DisplayName("동일한 쿠폰으로 여러 기기에서 동시에 주문해도, 쿠폰은 단 한번만 사용되어야 한다.")
	@Test
	void 동일한_쿠폰으로_여러_기기에서_동시에_주문해도_쿠폰은_단_한번만_사용되어야_한다() throws InterruptedException {
	    // arrange
		final int _1_COUNT = 1;
		UserEntity user = new UserEntity(
				"testUser",
				"테스트유저",
				Gender.M,
				"testUser@test.com",
				"2020-12-12"
		);
		UserEntity savedUser = userRepository.save(user);

		Coupon coupon = Coupon.builder()
				.name("coupon")
				.couponCode("couponCode")
				.type(CouponType.FIXED_AMOUNT)
				.discountFixedAmount(3000)
				.build();
		Coupon savedCoupon = couponRepository.save(coupon);

		UserCoupon userCoupon = UserCoupon.of(savedUser.getId(), savedCoupon.getId());
		userCouponRepository.save(userCoupon);

		int threadCount = 5;
		CountDownLatch latch = new CountDownLatch(threadCount);
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		AtomicInteger successCount = new AtomicInteger(0);

		// act
		for (int i = 1; i <= threadCount; i++) {
			executorService.submit(() -> {
				try {
					couponService.useCoupon(savedUser.getId(), savedCoupon.getId());
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

	    // assert
		UserCoupon foundUserCoupon = userCouponRepository.findByUserIdAndCouponId(savedUser.getId(), savedCoupon.getId()).get();
		assertThat(successCount.get()).isEqualTo(_1_COUNT);
		assertThat(foundUserCoupon.isUsed()).isTrue();

	}

}
