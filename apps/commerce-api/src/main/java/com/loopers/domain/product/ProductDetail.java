package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;

public record ProductDetail(Long productId, String productName, int price, int stock, int likeCount, Long brandId,
							String brandName, String brandDescription) {
	public static ProductDetail of(Product product, Brand brand) {
		return new ProductDetail(product.getId(),
				product.getName(),
				product.getPrice(),
				product.getStock(),
				product.getLikeCount(),
				brand != null ? brand.getId() : null,
				brand != null ? brand.getName() : null,
				brand != null ? brand.getDescription() : null
				);
	}

}
