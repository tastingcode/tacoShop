package com.loopers.application.like;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeInfoDto;

public record LikeInfo(Long userId, Long productId, boolean liked) {
	public static LikeInfo from(Like like) {
		return new LikeInfo(
				like.getUserId(),
				like.getProductId(),
				like.isLiked()
		);
	}
}
