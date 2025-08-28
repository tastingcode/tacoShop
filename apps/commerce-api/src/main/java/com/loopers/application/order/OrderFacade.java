package com.loopers.application.order;

import com.loopers.application.coupon.CouponService;
import com.loopers.application.point.PointService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderDomainService;
import com.loopers.domain.order.OrderProduct;
import com.loopers.domain.order.event.OrderCreatedEvent;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.user.UserDomainService;
import com.loopers.domain.user.UserEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderFacade {

	private final PointService pointService;
	private final UserDomainService userDomainService;
	private final OrderDomainService orderDomainService;
	private final CouponService couponService;
	private final OrderService orderService;
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	public OrderInfo createOrder(OrderCommand command) {
		// 유저 조회
		UserEntity user = userDomainService.getUserByUserId(command.userId());

		// 주문 상품 유효성 검증
		List<OrderProduct> orderProducts = orderService.getValidatedOrderProducts(command);

		// 주문 총액 계산
		int orderPrice = orderDomainService.calculateTotalPrice(orderProducts);

		// 할인 금액 계산
		int discountAmount = couponService.calcDiscountAmount(user.getId(), command.couponId(), orderPrice);

		// 재고 차감
		orderDomainService.deductStocks(orderProducts);

		// 주문 생성
		Order order = orderDomainService.createOrder(user, orderProducts, orderPrice, discountAmount, command.couponId());

		// 주문 상품 저장
		orderDomainService.saveOrderItems(order, orderProducts);

		// 주문 생성 이벤트 발행
		eventPublisher.publishEvent(OrderCreatedEvent.from(order));

		return OrderInfo.from(order);
	}

}
