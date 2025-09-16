package com.loopers.domain.order;


import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDomainService;
import com.loopers.domain.user.UserEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderDomainService {

	private final OrderRepository orderRepository;
	private final OrderProductRepository orderProductRepository;
	private final ProductDomainService productDomainService;

	@Transactional
	public Order createOrder(UserEntity user, List<OrderProduct> orderProducts, int orderPrice, int discountAmount, Long couponId) {
		Order order = Order.create(user, orderProducts, orderPrice, discountAmount, couponId);
		return orderRepository.save(order);
	}

	@Transactional
	public void deductStocks(List<OrderProduct> orderProducts) {
		orderProducts.forEach(orderProduct -> {
			Product product = productDomainService.getProductForUpdate(orderProduct.getProductId());
			product.deductStock(orderProduct.getQuantity());
		});
	}

	@Transactional
	public void restoreStocks(List<OrderProduct> orderProducts) {
		orderProducts.forEach(orderProduct -> {
			Product product = productDomainService.getProductForUpdate(orderProduct.getProductId());
			product.restoreStock(orderProduct.getQuantity());
		});
	}


	// 주문 상품 유효성 및 재고 검증
	public void validateOrderStock(List<OrderProduct> orderProducts, List<Product> products) {
		for (OrderProduct orderProduct : orderProducts) {
			Product product = products.stream()
					.filter(item -> item.getId().equals(orderProduct.getProductId()))
					.findFirst()
					.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다. 상품 ID: " + orderProduct.getProductId()));
			validateOrderProductStock(orderProduct, product);
		}
	}

	public void validateOrderProductStock(OrderProduct orderProduct, Product product) {
		if (orderProduct.getQuantity() <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 수량은 1 이상이어야 합니다.");
		}

		if (product.getStock() < orderProduct.getQuantity()) {
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
