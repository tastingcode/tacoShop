package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductJpaRepository extends JpaRepository<OrderProduct, Long> {
	List<OrderProduct> findAllByOrderId(Long orderId);

	List<OrderProduct> findAllByProductId(Long productId);

}
