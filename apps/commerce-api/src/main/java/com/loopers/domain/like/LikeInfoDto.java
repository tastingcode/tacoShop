package com.loopers.domain.like;

public record LikeInfoDto (Long userId, Long productId, int likeCount){
	public static LikeInfoDto from(Like like, int likeCount){
		return new LikeInfoDto(
				like.getUserId(),
				like.getProductId(),
				likeCount
		);
	}
}
