package com.loopers.application.order;

import com.loopers.application.point.PointService;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRepository;
import com.loopers.domain.coupon.*;
import com.loopers.domain.order.OrderDomainService;
import com.loopers.domain.order.OrderProduct;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRepository;
import com.loopers.domain.user.constant.Gender;
import com.loopers.infrastructure.coupon.UserCouponJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderFacadeTest {

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PointRepository pointRepository;

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private UserCouponJpaRepository userCouponRepository;

	@Autowired
	private PointService pointService;

	@Autowired
	OrderDomainService orderDomainService;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	private UserEntity user;
	private int chargeAmount;
	private Brand brand;
	private Product product1;
	private Product product2;
	private List<OrderProduct> orderProducts;
	private Coupon coupon;
	private UserCoupon userCoupon;
	private OrderCommand orderCommand;
	private int orderPrice;
	private int discountAmount;
	private int finalPrice;

	@BeforeEach
	void setUp() {
		// 사용자 생성 및 저장
		user = createAndSaveUser();

		chargeAmount = pointService.chargePoint(user.getUserId(), 300000).amount();

		// 브랜드 생성 및 저장
		brand = createAndSaveBrand();

		// 상품들 생성 및 저장
		product1 = createAndSaveProduct("상품A", 3000, 10, brand.getId());
		product2 = createAndSaveProduct("상품B", 5000, 10, brand.getId());

		// 쿠폰 생성 및 저장
		coupon = createAndSaveCoupon();

		// 사용자 쿠폰 발급
		userCoupon = createAndSaveUserCoupon(user.getId(), coupon.getId());

		// 주문 상품 생성
		orderProducts = createOrderProducts();

		// 주문 커맨드 생성
		orderCommand = new OrderCommand(user.getUserId(), orderProducts, coupon.getId());

		// 주문 총액 계산
		orderPrice = orderDomainService.calculateTotalPrice(orderProducts);

		// 쿠폰 할인 금액 계산
		discountAmount = coupon.calcDiscountAmount(orderPrice);

		// 최종 주문 금액
		finalPrice = orderPrice - discountAmount;
	}

	@AfterEach
	void cleanUp() {
		databaseCleanUp.truncateAllTables();
	}

	@DisplayName("주문 생성 성공 테스트")
	@Test
	void createOrder_Success() {
		// when
		OrderInfo orderInfo = orderFacade.createOrder(orderCommand);

		Coupon foundCoupon = couponRepository.findByCouponId(coupon.getId()).orElseThrow();

		UserCoupon foundUserCoupon = userCouponRepository.findByUserIdAndCouponId(user.getId(), coupon.getId()).orElseThrow();

		Point point = pointRepository.findByUserId(user.getId()).orElseThrow();

		// then
		assertThat(orderInfo).isNotNull();
		assertThat(orderInfo.finalPrice()).isEqualTo(finalPrice);
		assertThat(orderInfo.discountAmount()).isEqualTo(discountAmount);
		assertThat(orderInfo.status()).isEqualTo(OrderStatus.COMPLETED);
		assertThat(foundCoupon.getStatus()).isEqualTo(CouponStatus.USED);
		assertThat(foundUserCoupon.isUsed()).isTrue();
		assertThat(point.getAmount()).isEqualTo(chargeAmount - finalPrice);

	}


	// 사용자 생성 및 저장
	private UserEntity createAndSaveUser() {
		UserEntity user = new UserEntity(
				"testUser",
				"테스트유저",
				Gender.M,
				"testUser@test.com",
				"2020-12-12"
		);

		return userRepository.save(user);
	}

	// 브랜드 생성 및 저장
	private Brand createAndSaveBrand() {
		Brand brand = Brand.builder()
				.name("테스트 브랜드")
				.description("테스트 브랜드 설명")
				.build();
		return brandRepository.save(brand);
	}

	// 상품들 생성 및 저장
	private Product createAndSaveProduct(String name, int price, int stock, Long brandId) {
		Product product = Product.builder()
				.name(name)
				.price(price)
				.stock(stock)
				.brandId(brandId)
				.likeCount(0)
				.build();
		return productRepository.save(product);
	}

	// 쿠폰 생성 및 저장
	private Coupon createAndSaveCoupon() {
		Coupon coupon = Coupon.builder()
				.name("테스트 할인쿠폰")
				.couponCode("couponCode1")
				.type(CouponType.FIXED_AMOUNT)
				.minimumOrderPrice(10000)
				.discountFixedAmount(3000)
				.discountPercentage(0)
				.build();
		return couponRepository.save(coupon);
	}

	// 사용자 쿠폰 발급
	private UserCoupon createAndSaveUserCoupon(Long userId, Long couponId) {
		UserCoupon userCoupon = UserCoupon.of(userId, couponId);
		userCouponRepository.save(userCoupon);
		return userCoupon;
	}

	// 주문 상품 생성
	private List<OrderProduct> createOrderProducts() {
		List<OrderProduct> orderProducts = List.of(
				OrderProduct.of(null, product1.getId(), product1.getPrice(), 2),
				OrderProduct.of(null, product2.getId(), product2.getPrice(), 2)
		);

		return orderProducts;
	}

}
