package com.loopers.application.like;

import com.loopers.domain.like.LikeInfoDto;

public record LikeInfo(Long userId, Long productId, int likeCount) {
	public static LikeInfo from(LikeInfoDto likeInfoDto) {
		return new LikeInfo(
				likeInfoDto.userId(),
				likeInfoDto.productId(),
				likeInfoDto.likeCount()
		);
	}
}
