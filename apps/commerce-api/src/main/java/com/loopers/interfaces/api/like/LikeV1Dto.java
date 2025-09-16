package com.loopers.interfaces.api.like;


import com.loopers.application.like.LikeInfo;

public class LikeV1Dto {
	public record LikeRequest(
			Long productId
	) {
	}
	public record LikeResponse(
			Long userId,
			Long productId,
			boolean liked
	) {
		public static LikeResponse from(LikeInfo likeInfo){
			return new LikeResponse(likeInfo.userId(), likeInfo.productId(), likeInfo.liked());
		}
	}
}
