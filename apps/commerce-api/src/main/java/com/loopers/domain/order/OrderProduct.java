package com.loopers.domain.order;


import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "order_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct extends BaseEntity {
	@Column(name = "ref_order_id")
	private Long orderId;
	@Column(name = "ref_product_id", nullable = false)
	private Long productId;
	@Column(nullable = false)
	private int price;
	@Column(nullable = false)
	private int quantity;

	public static OrderProduct of(Long orderId, Long productId, int price, int quantity) {
		OrderProduct orderProduct = new OrderProduct();
		orderProduct.orderId = orderId;
		orderProduct.productId = productId;
		orderProduct.price = price;
		orderProduct.quantity = quantity;
		return orderProduct;
	}
}
