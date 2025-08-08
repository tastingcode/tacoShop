package com.loopers.application.order;

import com.loopers.domain.order.OrderProduct;
import com.loopers.domain.order.OrderRepository;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderUseCase {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	@Transactional
	public void deductStocks(List<OrderProduct> orderProducts, List<Product> products) {
		orderProducts.forEach(orderProduct -> {
			Product product = products.stream()
					.filter(p -> p.getId().equals(orderProduct.getProductId()))
					.findFirst()
					.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다." ));
			product.deductStock(orderProduct.getQuantity());
		});
	}


}
