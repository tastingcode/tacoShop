package com.loopers.application.order;


import com.loopers.domain.order.OrderDomainService;
import com.loopers.domain.order.OrderProduct;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderDomainService orderDomainService;
	private final ProductDomainService productDomainService;

	public List<OrderProduct> getValidatedOrderProducts(OrderCommand command) {
		// 주문 상품 변환
		List<OrderProduct> orderProducts = OrderProductCommand.toOrderProducts(command.orderProducts());

		// 상품 목록 조회 by 주문 상품
		List<Product> products = productDomainService.getProductsByOrderProducts(orderProducts);

		// 주문 상품 유효성 및 재고 검증
		orderDomainService.validateOrderStock(orderProducts, products);

		return orderProducts;
	}

}
