package com.loopers.application.product;

import com.loopers.domain.product.ProductDetail;

public record ProductInfo(Long productId, String productName, int price, int stock, int likeCount, Long brandId,
						  String brandName, String brandDescription) {
	public static ProductInfo from(ProductDetail productDetail) {
		return new ProductInfo(
				productDetail.productId(),
				productDetail.productName(),
				productDetail.price(),
				productDetail.stock(),
				productDetail.likeCount(),
				productDetail.brandId() != null ? productDetail.brandId() : null,
				productDetail.brandName() != null ? productDetail.brandName() : null,
				productDetail.brandDescription() != null ? productDetail.brandDescription() : null);
	}
}
