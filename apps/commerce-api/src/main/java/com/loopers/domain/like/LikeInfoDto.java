package com.loopers.domain.like;

public record LikeInfoDto (Long userId, Long productId, boolean liked){
	public static LikeInfoDto from(Like like, boolean liked){
		return new LikeInfoDto(
				like.getUserId(),
				like.getProductId(),
				liked
		);
	}
}
