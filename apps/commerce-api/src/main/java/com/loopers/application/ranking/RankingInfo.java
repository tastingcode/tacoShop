package com.loopers.application.ranking;

import com.loopers.domain.product.Product;

public record RankingInfo(
		Long productId,
		String productName,
		int price,
		int stock,
		Long brandId,
		int likeCount,
		long rank
) {
	public static RankingInfo of(Product p, long rank) {
		return new RankingInfo(
				p.getId(),
				p.getName(),
				p.getPrice(),
				p.getStock(),
				p.getBrandId(),
				p.getLikeCount(),
				rank
		);
	}
}
