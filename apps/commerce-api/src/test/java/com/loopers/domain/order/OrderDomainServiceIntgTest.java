package com.loopers.domain.order;

import com.loopers.domain.product.Product;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRepository;
import com.loopers.domain.user.constant.Gender;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderDomainServiceIntgTest {

	@Autowired
	private OrderDomainService orderDomainService;

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	@Autowired
	private UserRepository userRepository;

	@AfterEach
	void cleanDatabase() {
		databaseCleanUp.truncateAllTables();
	}

	@DisplayName("동일한 상품에 대해 여러 주문이 동시에 요청되어도, 재고가 정상적으로 차감되어야 한다.")
	@Test
	void 동일한_상품에_대해_여러_주문이_동시에_요청되어도_재고가_정상적으로_차감되어야_한다() throws InterruptedException {
		// arrange
		int initStock = 100;
		Product product = Product.builder()
				.name("상품명")
				.price(3000)
				.stock(initStock)
				.brandId(1L)
				.likeCount(0)
				.build();
		Product savedProduct = productJpaRepository.save(product);
		List<Product> products = List.of(savedProduct, savedProduct, savedProduct);

		int orderQuantity = 5;
		List<OrderProduct> orderProducts = List.of(
				OrderProduct.of(null, savedProduct.getId(), savedProduct.getPrice(), orderQuantity),
				OrderProduct.of(null, savedProduct.getId(), savedProduct.getPrice(), orderQuantity),
				OrderProduct.of(null, savedProduct.getId(), savedProduct.getPrice(), orderQuantity)
		);
		int totalOrderQuantity = 0;
		for (OrderProduct orderProduct : orderProducts) {
			totalOrderQuantity += orderProduct.getQuantity();
		}

		int threadCount = 3;
		CountDownLatch latch = new CountDownLatch(threadCount);
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		AtomicInteger successCount = new AtomicInteger(0);

		// act
		for (int i = 1; i <= 3; i++) {
			executorService.submit(() -> {
				try {
					orderDomainService.deductStocks(orderProducts);
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
		Product foundProduct = transactionTemplate.execute(status ->
				productJpaRepository.findById(savedProduct.getId())).get();
		assertThat(successCount.get()).isEqualTo(3);
		assertThat(foundProduct.getStock()).isEqualTo(initStock - threadCount * totalOrderQuantity);
	}

	@DisplayName("재고 복구 테스트")
	@Test
	void 재고_복구_테스트() {
		// arrange
		UserEntity userEntity = new UserEntity(
				"testUser",
				"테스트유저",
				Gender.M,
				"testUser@test.com",
				"2020-12-12"
		);
		UserEntity user = userRepository.save(userEntity);

		int initStock = 100;
		Product product = Product.builder()
				.name("상품명")
				.price(3000)
				.stock(initStock)
				.brandId(1L)
				.likeCount(0)
				.build();
		Product savedProduct = productJpaRepository.save(product);
		List<Product> products = List.of(savedProduct, savedProduct, savedProduct);
		int totalInitStock = initStock * products.size();

		int orderQuantity = 5;
		List<OrderProduct> orderProductList = List.of(
				OrderProduct.of(null, savedProduct.getId(), savedProduct.getPrice(), orderQuantity),
				OrderProduct.of(null, savedProduct.getId(), savedProduct.getPrice(), orderQuantity),
				OrderProduct.of(null, savedProduct.getId(), savedProduct.getPrice(), orderQuantity)
		);
		Order order = orderDomainService.createOrder(user, orderProductList, 30000, 10000, 1L);
		List<OrderProduct> orderProducts= order.getOrderProducts();


		// act
		orderDomainService.deductStocks(orderProducts);
		orderDomainService.restoreStocks(orderProducts);
		int currentTotalStock = 0;
		for (Product p : products) {
			currentTotalStock += p.getStock();
		}

		// assert
		assertThat(totalInitStock).isEqualTo(currentTotalStock);
	}

}
