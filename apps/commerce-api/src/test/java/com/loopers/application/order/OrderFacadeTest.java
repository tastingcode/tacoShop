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
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

	@DisplayName("사용 불가능하거나 존재하지 않는 쿠폰일 경우 주문은 실패해야 한다.")
	@Test
	void 사용_불가능하거나_존재하지_않는_쿠폰일_경우_주문은_실패해야_한다() {
		// arrange
		Long notExistCouponId = -1L;
		OrderCommand wrongOrderCommand = new OrderCommand(user.getUserId(), orderProducts, notExistCouponId);

		// act && assert
		assertThatThrownBy(() -> orderFacade.createOrder(wrongOrderCommand))
				.isInstanceOf(CoreException.class);
	}

	@DisplayName("재고가 존재하지 않거나 부족할 경우 주문은 실패해야 한다.")
	@Test
	void 재고가_존재하지_않거나_부족할_경우_주문은_실패해야_한다() {
		// arrange
		Product product1 = createAndSaveProduct("상품A", 3000, 0, brand.getId());
		Product product2 = createAndSaveProduct("상품A", 3000, 0, brand.getId());

		List<OrderProduct> orderProducts = List.of(
				OrderProduct.of(null, product1.getId(), product1.getPrice(), 2),
				OrderProduct.of(null, product2.getId(), product2.getPrice(), 2)
		);

		orderCommand = new OrderCommand(user.getUserId(), orderProducts, coupon.getId());

		// act && assert
		assertThatThrownBy(() -> orderFacade.createOrder(orderCommand))
				.isInstanceOf(CoreException.class);
	}

	@DisplayName("주문 시 유저의 포인트 잔액이 부족할 경우 주문은 실패해야 한다")
	@Test
	void 주문_시_유저의_포인트_잔액이_부족할_경우_주문은_실패해야_한다() {
		// arrange
		UserEntity user = new UserEntity(
				"zeroPoint",
				"테스트유저",
				Gender.M,
				"testUser@test.com",
				"2020-12-12"
		);
		UserEntity savedUser = userRepository.save(user);

		final int _10O0_POINT = 1000;
		pointService.chargePoint(savedUser.getUserId(), _10O0_POINT).amount();

		orderCommand = new OrderCommand(savedUser.getUserId(), orderProducts, coupon.getId());

		// act && assert
		assertThatThrownBy(() -> orderFacade.createOrder(orderCommand))
				.isInstanceOf(CoreException.class);
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
