package com.loopers.application.order;

import com.loopers.application.coupon.CouponService;
import com.loopers.application.point.PointService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderProduct;
import com.loopers.domain.order.OrderDomainService;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderFacade {

	private final PointService pointService;
	private final UserService userService;
	private final OrderDomainService orderDomainService;
	private final ProductRepository productRepository;
	private final CouponService couponService;

	public OrderInfo createOrder(OrderCommand command){
		// 유저 조회
		UserEntity user = getVerifiedUser(command.userId());

		// 주문 상품 조회
		List<Long> productIds = command.orderProducts().stream()
				.map(OrderProduct::getProductId)
				.toList();
		List<Product> products = productRepository.findAllById(productIds);

		List<OrderProduct> orderProducts = command.orderProducts();

		// 주문 상품 유효성 검증
		orderDomainService.validateOrderItems(orderProducts, products);

		// 주문 총액 계산
		int orderPrice = orderDomainService.calculateTotalPrice(orderProducts);

		// 할인 금액 계산
		int discountAmount = couponService.calcDiscountAmount(user.getId(), command.couponId(), orderPrice);

		// 주문 생성
		Order order = Order.create(user, orderProducts, orderPrice, discountAmount);

		// 포인트 차감
		pointService.useMyPoint(command.userId(), order.getFinalPrice());

		// 재고 차감
		orderDomainService.deductStocks(orderProducts, products);

		// 쿠폰 사용
		couponService.useCoupon(user.getId(), command.couponId());

		// 주문 완료
		order.updateStatus(OrderStatus.COMPLETED);

		// 주문 상품 저장
		orderDomainService.saveOrderItems(order, orderProducts);

		return OrderInfo.from(order);
	}

	private UserEntity getVerifiedUser(String userId) {
		UserEntity user = userService.getUser(userId);
		if (user == null) {
			throw new CoreException(ErrorType.NOT_FOUND, "로그인 한 회원만 이용할 수 있습니다.");
		}
		return user;
	}

}
