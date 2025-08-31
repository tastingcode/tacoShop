package com.loopers.domain.like.event;

import com.loopers.domain.like.Like;

public record ProductLikeEvent(
		Long productId,
		Long userId,
		LikeEventType likeEventType
) {
	public static ProductLikeEvent of(Like like, LikeEventType likeEventType) {
		return new ProductLikeEvent(like.getProductId(), like.getUserId(), likeEventType);
	}
}
