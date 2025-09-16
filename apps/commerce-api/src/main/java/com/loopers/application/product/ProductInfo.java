package com.loopers.application.product;

import com.loopers.domain.product.ProductDetail;

public record ProductInfo(Long productId, String productName, int price, int stock, int likeCount, Long brandId,
						  String brandName, String brandDescription, Long rank) {
	public static ProductInfo from(ProductDetail productDetail) {
		return new ProductInfo(
				productDetail.productId(),
				productDetail.productName(),
				productDetail.price(),
				productDetail.stock(),
				productDetail.likeCount(),
				productDetail.brandId(),
				productDetail.brandName(),
				productDetail.brandDescription(),
				null
		);
	}

	public ProductInfo withRank(Long rank) {
		return new ProductInfo(productId,
				productName,
				price,
				stock,
				likeCount,
				brandId,
				brandName,
				brandDescription,
				rank);
	}
}
