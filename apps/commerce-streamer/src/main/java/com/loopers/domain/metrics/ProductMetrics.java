package com.loopers.domain.metrics;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "product_metrics")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductMetrics {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "likes_delta")
	private int likesDelta;

	@Column(name = "sales_delta")
	private int salesDelta;

	@Column(name = "views_delta")
	private int viewsDelta;

	private LocalDate date;

	public static ProductMetrics of(Long productId){
		ProductMetrics productMetrics = new ProductMetrics();
		productMetrics.productId = productId;
		productMetrics.date = LocalDate.now();
		return productMetrics;
	}

	public void adjustLikesDelta(int delta){
		this.likesDelta += delta;
	}

	public void increaseSalesDelta(int delta){
		this.salesDelta += delta;
	}

	public void increaseViewsDelta(int delta){
		this.viewsDelta += delta;
	}

}
