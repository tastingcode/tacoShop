package com.loopers.domain.order;

import java.util.List;
import java.util.Optional;

public interface OrderProductRepository {
	OrderProduct save(OrderProduct orderItem);

	Optional<OrderProduct> findById(Long id);

	List<OrderProduct> findAllByOrderId(Long orderId);

	List<OrderProduct> findAllByProductId(Long productId);
}
