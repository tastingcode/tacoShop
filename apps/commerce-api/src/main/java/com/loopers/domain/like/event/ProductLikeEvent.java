package com.loopers.domain.like.event;

import com.loopers.domain.event.DomainEvent;
import com.loopers.domain.like.Like;

import java.util.UUID;

public record ProductLikeEvent(
		String eventId,
		Long productId,
		Long userId,
		LikeEventType likeEventType
) implements DomainEvent {
	public static ProductLikeEvent of(Like like, LikeEventType likeEventType) {
		return new ProductLikeEvent(UUID.randomUUID().toString(), like.getProductId(), like.getUserId(), likeEventType);
	}
}
