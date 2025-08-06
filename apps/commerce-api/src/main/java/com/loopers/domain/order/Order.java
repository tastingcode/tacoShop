package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
	@Column(name = "ref_user_id", nullable = false, updatable = false)
	private Long userId;

	@Embedded
	private OrderDate orderDate;

	@Enumerated(EnumType.STRING)
	private OrderStatus status = OrderStatus.PENDING;

	@Column(name = "final_price", nullable = false)
	private int finalPrice;

	@Column(name = "discount_amount")
	private int discountAmount;

	@Transient
	private List<OrderProduct> orderProducts = new ArrayList<>();

	public static Order create(UserEntity user, List<OrderProduct> orderProducts, int orderPrice, int discountAmount) {
		Order order = new Order();
		for (OrderProduct orderProduct : orderProducts) {
			order.addProduct(orderProduct);
		}

		order.userId = user.getId();
		order.orderDate = OrderDate.of(LocalDateTime.now());
		order.status = OrderStatus.PENDING;
		order.finalPrice = orderPrice - discountAmount;
		order.discountAmount = discountAmount;
		return order;
	}

	public void addProduct(OrderProduct orderProduct) {
		if (orderProduct == null) {
			throw new IllegalArgumentException("주문 아이템은 null일 수 없습니다.");
		}

		orderProducts.add(orderProduct);
	}

	public void updateStatus(OrderStatus status) {
		this.status = status;
	}

}
