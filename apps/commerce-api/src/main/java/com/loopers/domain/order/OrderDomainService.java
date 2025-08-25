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
public class OrderDomainService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final OrderProductRepository orderProductRepository;

	@Transactional
	public Order createOrder(UserEntity user, List<OrderProduct> orderProducts, int orderPrice, int discountAmount, Long couponId) {
		Order order = Order.create(user, orderProducts, orderPrice, discountAmount, couponId);
		return orderRepository.save(order);
	}

	@Transactional
	public void deductStocks(List<OrderProduct> orderProducts) {
		orderProducts.forEach(orderProduct -> {
			Product product = productRepository.findByIdForUpdate(orderProduct.getProductId())
					.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다. 상품 ID: "));
			product.deductStock(orderProduct.getQuantity());
		});
	}

	@Transactional
	public void restoreStocks(List<OrderProduct> orderProducts) {
		orderProducts.forEach(orderProduct -> {
			Product product = productRepository.findByIdForUpdate(orderProduct.getProductId())
					.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다. 상품 ID: "));
			product.restoreStock(orderProduct.getQuantity());
		});
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

	// 주문 총액 계산
	public int calculateTotalPrice(List<OrderProduct> orderProducts) {
		return orderProducts.stream()
				.mapToInt(item -> item.getPrice() * item.getQuantity())
				.sum();
	}

	// 주문 상품 저장
	public void saveOrderItems(Order order, List<OrderProduct> orderProducts) {
		orderProducts.forEach(item -> {
			OrderProduct orderProduct = OrderProduct.of(order, item.getProductId(), item.getPrice(), item.getQuantity());
			orderProductRepository.save(orderProduct);
		});
	}

	public Order getOrder(Long orderId) {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 주문 이력을 찾을 수 없습니다."));
	}


}
