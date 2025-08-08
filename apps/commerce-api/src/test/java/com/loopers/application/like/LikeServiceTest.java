package com.loopers.application.like;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRepository;
import com.loopers.domain.user.constant.Gender;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LikeServiceTest {

	@Autowired
	private LikeService likeService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;


	@BeforeEach
	void setUp() {
		for (int i = 1; i <= 5; i++) {
			UserEntity user = new UserEntity(
					"testUser" + i,
					"테스트유저",
					Gender.M,
					"testUser@test.com",
					"2020-12-12"
			);
			userRepository.save(user);
		}

	}

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	@DisplayName("동일한 상품에 대해 여러명이 좋아요를 요청해도, 상품의 좋아요 개수가 정상 반영되어야 한다.")
	@Test
	void 동일한_상품에_대해_여러명이_좋아요를_요청해도_상품의_좋아요_개수가_정상_반영되어야_한다() throws InterruptedException {
		// arrange
		int initLikeCount = 0;
		Product product = Product.builder()
				.name("testProduct")
				.price(100000)
				.stock(100)
				.brandId(1L)
				.likeCount(initLikeCount)
				.build();
		Product savedProduct = productRepository.save(product);

		int threadCount = 5;
		CountDownLatch latch = new CountDownLatch(threadCount);
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		AtomicInteger successCount = new AtomicInteger(0);

		// act
		for (int i = 1; i <= threadCount; i++) {
			String userId = "testUser" + i;
			executorService.submit(() -> {
				try {
					likeService.like(userId, savedProduct.getId());
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
		Product foundProduct = productRepository.findById(product.getId()).get();

		assertThat(successCount.get()).isEqualTo(threadCount);
		assertThat(foundProduct.getLikeCount()).isEqualTo(initLikeCount + threadCount);
	}

	@DisplayName("동일한 상품에 대해 여러명이 좋아요 취소를 요청해도, 상품의 좋아요 개수가 정상 반영되어야 한다.")
	@Test
	void 동일한_상품에_대해_여러명이_좋아요_취소를_요청해도_상품의_좋아요_개수가_정상_반영되어야_한다() throws InterruptedException {
		// arrange
		Product product = Product.builder()
				.name("testProduct")
				.price(100000)
				.stock(100)
				.brandId(1L)
				.likeCount(0)
				.build();
		Product savedProduct = productRepository.save(product);
		for (int i = 1; i <= 5; i++) {
			String userId = "testUser" + i;
			likeService.like(userId, savedProduct.getId());
		}
		int initProductLikeCount = productRepository.findById(savedProduct.getId()).get()
				.getLikeCount();


		int threadCount = 5;
		CountDownLatch latch = new CountDownLatch(threadCount);
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		AtomicInteger successCount = new AtomicInteger(0);


		// act
		for (int i = 1; i <= threadCount; i++) {
			String userId = "testUser" + i;
			executorService.submit(() -> {
				try {
					likeService.unLike(userId, savedProduct.getId());
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
		Product foundProduct = productRepository.findById(savedProduct.getId()).get();

		assertThat(successCount.get()).isEqualTo(threadCount);
		assertThat(foundProduct.getLikeCount()).isEqualTo(initProductLikeCount-threadCount);
	}


}
