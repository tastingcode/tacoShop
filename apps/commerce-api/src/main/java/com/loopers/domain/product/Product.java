package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private int price;
	@Column(nullable = false)
	private int stock;
	@Column(name = "ref_brand_id", nullable = false)
	private Long brandId;
	private int likeCount;

	@Builder
	public Product (String name, int price, int stock, Long brandId, int likeCount) {
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.brandId = brandId;
		this.likeCount = likeCount;
	}

	public void deductStock(int quantity) {
		if (quantity <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "1개 이상 주문할 수 있습니다.");
		}
		if (this.stock < quantity) {
			throw new CoreException(ErrorType.BAD_REQUEST, "재고가 부족합니다.");
		}
		this.stock -= quantity;
	}

	public void increaseLikeCount() {
		this.likeCount++;
	}

	public void decreaseLikeCount() {
		this.likeCount--;
	}
}
