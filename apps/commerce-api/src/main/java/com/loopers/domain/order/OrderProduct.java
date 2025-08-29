package com.loopers.domain.order;


import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "order_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct extends BaseEntity {
	@ManyToOne
	@JoinColumn(name = "ref_order_id")
	private Order order;

	@Column(name = "ref_product_id", nullable = false)
	private Long productId;
	@Column(nullable = false)
	private int price;
	@Column(nullable = false)
	private int quantity;

	public static OrderProduct of(Order order, Long productId, int price, int quantity) {
		OrderProduct orderProduct = new OrderProduct();
		orderProduct.order = order;
		orderProduct.productId = productId;
		orderProduct.price = price;
		orderProduct.quantity = quantity;
		return orderProduct;
	}
}
