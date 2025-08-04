package com.loopers.application.order;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderProduct;
import com.loopers.domain.order.OrderService;
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

	private final UserService userService;
	private final OrderService orderService;
	private final ProductRepository productRepository;

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
		orderService.validateOrderItems(orderProducts, products);

		// 주문 생성
		Order order = orderService.createOrder(user, orderProducts, products);

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
