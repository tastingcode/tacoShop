package com.loopers.domain.order;


import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.user.UserEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	@Transactional
	public Order createOrder(UserEntity userEntity, List<OrderProduct> orderProducts, List<Product> products) {
		Order order = Order.create(userEntity, orderProducts);

		// 재고 차감
		orderProducts.forEach(orderProduct -> {
			Product product = products.stream()
					.filter(p -> p.getId().equals(orderProduct.getProductId()))
					.findFirst()
					.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다." ));
			product.deductStock(orderProduct.getQuantity());
		});

		// 포인트 차감
		int totalPrice = order.calculateTotalPrice();
		userEntity.usePoint(totalPrice);

		// 주문 상태 변경
		order.updateOrderStatus(OrderStatus.COMPLETED);

		// 주문 저장
		return save(order, products);
	}

	public Order save(Order order, List<Product> products) {
		// 상품 저장
		productRepository.saveAll(products);

		return orderRepository.save(order);
	}

	/**
	 * 주문 아이템 리스트 검증
	 */
	public void validateOrderItems(List<OrderProduct> orderProducts, List<Product> products) {
		for (OrderProduct orderProduct : orderProducts) {
			Product product = products.stream()
					.filter(item -> item.getId().equals(orderProduct.getProductId()))
					.findFirst()
					.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다. 상품 ID: " + orderProduct.getProductId()));
			validateOrderItem(orderProduct, product);
		}
	}

	private void validateOrderItem(OrderProduct orderItem, Product product) {
		if (orderItem.getQuantity() <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 수량은 1 이상이어야 합니다.");
		}

		if (product.getStock() < orderItem.getQuantity()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "재고가 부족합니다.");
		}
	}
}
