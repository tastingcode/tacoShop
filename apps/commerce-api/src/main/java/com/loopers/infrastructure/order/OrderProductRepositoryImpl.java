package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderProduct;
import com.loopers.domain.order.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OrderProductRepositoryImpl implements OrderProductRepository {
	private final OrderProductJpaRepository orderProductJpaRepository;

	@Override
	public OrderProduct save(OrderProduct orderProduct) {
		return orderProductJpaRepository.save(orderProduct);
	}

	@Override
	public Optional<OrderProduct> findById(Long id) {
		return orderProductJpaRepository.findById(id);
	}

	@Override
	public List<OrderProduct> findAllByOrderId(Long orderId) {
		return orderProductJpaRepository.findAllByOrderId(orderId);
	}

	@Override
	public List<OrderProduct> findAllByProductId(Long productId) {
		return orderProductJpaRepository.findAllByProductId(productId);
	}

}
