package com.loopers.domain.like;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LikeTest {

	/**
	 * 좋아요 단위 테스트
	 * - [x] 좋아요 등록 시 좋아요 상태가 true 로 변경된다.
	 * - [x] 좋아요 취소 시 좋아요 상태가 false 로 변경된다.
	 * - [x] 좋아요 중복 등록 시 좋아요 상태는 true 로 유지된다.
	 * - [x] 좋아요 중복 취소 시 좋아요 상태는 false 로 유지된다.
	 */
	@DisplayName("좋아요 등록 시 좋아요 상태가 true 로 변경된다.")
	@Test
	void likedIsTrueWhenLike() {
		// arrange
		Like like = Like.of(1L, 1L);

		// act
		like.like();

		// assert
		assertThat(like.isLiked()).isTrue();
	}

	@DisplayName("좋아요 취소 시 좋아요 상태가 false 로 변경된다.")
	@Test
	void likedIsFalseWhenLike() {
		// arrange
		Like like = Like.of(1L, 1L);

		like.like();
		// act
		like.unLike();

		// assert
		assertThat(like.isLiked()).isFalse();
	}

	@DisplayName("좋아요 중복 등록 시 좋아요 상태는 true 로 유지된다.")
	@Test
	void likedIsTrueWhenLikeManyTimes() {
		// arrange
		Like like = Like.of(1L, 1L);

		// act
		like.like();
		like.like();
		like.like();

		// assert
		assertThat(like.isLiked()).isTrue();
	}

	@DisplayName("좋아요 중복 취소 시 좋아요 상태는 false 로 유지된다.")
	@Test
	void likedIsFalseWhenLikeManyTimes() {
		// arrange
		Like like = Like.of(1L, 1L);

		// act
		like.unLike();
		like.unLike();
		like.unLike();

		// assert
		assertThat(like.isLiked()).isFalse();
	}
}
