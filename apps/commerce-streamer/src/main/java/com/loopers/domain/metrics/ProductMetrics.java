package com.loopers.domain.metrics;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "product_metrics")
@NoArgsConstructor
public class ProductMetrics {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "event_type", nullable = false)
	private String eventType;

	@Enumerated(EnumType.STRING)
	private ProductMetricType productMetricType;

	private int delta;

	private LocalDate date;


	public static ProductMetrics create(Long ProductId, String eventType, ProductMetricType productMetricType, int delta){
		ProductMetrics productMetrics = new ProductMetrics();
		productMetrics.productId = ProductId;
		productMetrics.eventType = eventType;
		productMetrics.productMetricType = productMetricType;
		productMetrics.delta = delta;
		productMetrics.date = LocalDate.now();
		return productMetrics;
	}

}
