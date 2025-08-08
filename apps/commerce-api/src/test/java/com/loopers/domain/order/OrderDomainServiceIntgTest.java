package com.loopers.domain.order;

import com.loopers.application.coupon.CouponService;
import com.loopers.domain.product.Product;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
					orderDomainService.deductStocks(orderProducts, products);
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

}
