package com.loopers.application.like;

import com.loopers.domain.like.event.LikeEventType;
import com.loopers.domain.like.event.ProductLikeEvent;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRepository;
import com.loopers.domain.user.constant.Gender;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@RecordApplicationEvents
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

	@Autowired
	ApplicationEvents applicationEvents;


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


	/**
	 * 좋아요 동시성 테스트
	 * -[x] 동일한 상품에 대해 여러명이 좋아요를 요청해도, 상품의 좋아요 개수가 정상 반영되어야 한다.
	 * -[x] 동일한 상품에 대해 여러명이 좋아요 취소를 요청해도, 상품의 좋아요 개수가 정상 반영되어야 한다.
	 */
	@Nested
	@DisplayName("좋아요 동시성 테스트")
	class LikeConcurrency{
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
			await().atMost(Duration.ofSeconds(2))
					.untilAsserted(
							() -> {
								assertThat(applicationEvents.stream(ProductLikeEvent.class).count())
										.isEqualTo(threadCount);
							}
					);


			await().atMost(Duration.ofSeconds(2))
					.untilAsserted(() -> {
						Product foundProduct = productRepository.findById(savedProduct.getId()).orElseThrow();
						assertThat(foundProduct.getLikeCount()).isEqualTo(initLikeCount + threadCount);
					});

			assertThat(successCount.get()).isEqualTo(threadCount);
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

			await().atMost(Duration.ofSeconds(2))
					.untilAsserted(() -> {
						Product foundProduct = productRepository.findById(savedProduct.getId()).orElseThrow();
						assertThat(foundProduct.getLikeCount()).isEqualTo(5);
					});


			int initLikeCount = productRepository.findById(savedProduct.getId()).get()
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
						likeService.unlike(userId, savedProduct.getId());
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
			await().atMost(Duration.ofSeconds(2))
					.untilAsserted(() -> {
						Product foundProduct = productRepository.findById(savedProduct.getId()).orElseThrow();
						assertThat(foundProduct.getLikeCount()).isEqualTo(initLikeCount -threadCount);
					});

			assertThat(successCount.get()).isEqualTo(threadCount);
		}
	}

	/**
	 * 이벤트 검증 - 이벤트 기반으로 좋아요 처리와 집계를 분리한다.
	 * -[x] 좋아요 이벤트 검증
	 * -[x] 좋아요 취소 이벤트 검증
	 */
	@Nested
	@DisplayName("이벤트 검증 - 이벤트 기반으로 좋아요 처리와 집계를 분리한다.")
	class EventVerification {
		@DisplayName("좋아요 이벤트 검증")
		@Test
		void likeEventVerification() {
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

			// act
			likeService.like("testUser1", savedProduct.getId());
			ProductLikeEvent productLikeEvent = applicationEvents.stream(ProductLikeEvent.class).findFirst().orElseThrow();


			// assert
			assertThat(applicationEvents.stream(ProductLikeEvent.class).count()).isEqualTo(1);
			await().atMost(Duration.ofSeconds(1))
					.untilAsserted(
							() -> {
								Product updatedProduct = productRepository.findById(savedProduct.getId()).get();
								assertThat(updatedProduct.getLikeCount()).isEqualTo(initLikeCount + 1);
							}
					);
			assertThat(savedProduct.getId()).isEqualTo(productLikeEvent.productId());
		}

		@DisplayName("좋아요 취소 이벤트 검증")
		@Test
		void UnlikeEventVerification() {
			// arrange
			int initLikeCount = 10;
			Product product = Product.builder()
					.name("testProduct")
					.price(100000)
					.stock(100)
					.brandId(1L)
					.likeCount(initLikeCount)
					.build();
			Product savedProduct = productRepository.save(product);

			// act
			likeService.like("testUser1", savedProduct.getId());
			await().atMost(Duration.ofSeconds(1))
					.untilAsserted(
							() -> {
								Product updatedProduct = productRepository.findById(savedProduct.getId()).get();
								assertThat(updatedProduct.getLikeCount()).isEqualTo(initLikeCount + 1);
							}
					);

			// 좋아요 취소
			likeService.unlike("testUser1", savedProduct.getId());
			ProductLikeEvent productUnlikeEvent = applicationEvents.stream(ProductLikeEvent.class)
					.filter(e -> e.likeEventType() == LikeEventType.UNLIKE)
					.findFirst()
					.orElseThrow();

			// assert
			assertThat(applicationEvents.stream(ProductLikeEvent.class).count()).isEqualTo(2);
			await().atMost(Duration.ofSeconds(1))
					.untilAsserted(
							() -> {
								Product updatedProduct = productRepository.findById(savedProduct.getId()).get();
								assertThat(updatedProduct.getLikeCount()).isEqualTo(initLikeCount);
							}
					);

			assertThat(savedProduct.getId()).isEqualTo(productUnlikeEvent.productId());

		}
	}
}
