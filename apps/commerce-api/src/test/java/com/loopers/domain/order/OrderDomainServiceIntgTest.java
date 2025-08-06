package com.loopers.domain.order;

import com.loopers.application.coupon.CouponService;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderDomainServiceIntgTest {

	@Autowired
	private OrderDomainService orderDomainService;

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@Autowired
	private BrandJpaRepository brandJpaRepository;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Autowired
	private CouponService couponService;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;


	@AfterEach
	void cleanDatabase() {
		databaseCleanUp.truncateAllTables();
	}

	/*@DisplayName("주문 생성 시,")
	@Nested
	class createOrder {

		@DisplayName("정상적으로 주문이 생성되고 재고 및 포인트가 차감된다.")
		@Test
		void createOrder_success() {
			// arrange
			// 브랜드 생성 및 저장
			Brand brand = Brand.builder()
					.name("테스트 브랜드")
					.description("테스트 브랜드 설명")
					.build();
			Brand savedBrand = brandJpaRepository.save(brand);

			// 상품 생성 및 저장
			Product product = Product.builder()
					.name("상품명")
					.price(10000)
					.stock(10)
					.brandId(savedBrand.getId())
					.likeCount(0)
					.build();
			Product savedProduct = productJpaRepository.save(product);

			// 사용자 생성 및 저장
			UserEntity user = new UserEntity("testUser", "테스트유저", Gender.M, "testUser@test.com", "2020-12-12");
			user.addPoint(50000L);
			UserEntity savedUser = userJpaRepository.save(user);

			// 주문 상품 생성
			OrderProduct orderProduct = OrderProduct.of(null, savedProduct.getId(), savedProduct.getPrice(), 2);
			List<OrderProduct> orderProducts = List.of(orderProduct);

			// act
			Order order = orderDomainService.createOrder(savedUser, orderProducts, List.of(savedProduct));

			// assert
			assertAll(
					() -> assertThat(order).isNotNull(),
					() -> assertThat(order.getFinalPrice()).isEqualTo(20000),
					() -> assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED),
					() -> assertThat(savedProduct.getStock()).isEqualTo(8),
					() -> assertThat(savedUser.getPoint()).isEqualTo(30000L)
			);
		}
	}*/
}
