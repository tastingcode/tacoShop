package com.loopers.domain.like;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDetail;
import com.loopers.domain.user.UserEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class LikeDomainService {
	private final LikeRepository likeRepository;

	public LikeInfoDto like(UserEntity user, Product product) {
		// like 생성
		Optional<Like> optionalLike = findByUserIdAndProductId(user.getId(), product.getId());
		Like like = getLikeOrCreate(optionalLike, user, product);

		// 좋아요 처리
		if (!like.isLiked())
			product.increaseLikeCount();
		like.like();

		save(like);
		int likeCount = product.getLikeCount();
		return LikeInfoDto.from(like, likeCount);
	}

	public LikeInfoDto unLike(UserEntity user, Product product) {
		// like 생성
		Like like = findByUserIdAndProductId(user.getId(), product.getId())
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "좋아요 한 상품만 좋아요를 취소 할 수 있습니다."));

		// 좋아요 취소 처리
		if (like.isLiked())
			product.decreaseLikeCount();
		like.unLike();

		int likeCount = product.getLikeCount();
		return LikeInfoDto.from(like, likeCount);
	}

	public List<ProductDetail> getLikedProducts(UserEntity user) {
		return likeRepository.findLikedProducts(user.getId());
	}

	public Like getLikeOrCreate(Optional<Like> optionalLike, UserEntity user, Product product) {
		if (optionalLike.isPresent()) {
			return optionalLike.get();
		}

		return Like.of(user.getId(), product.getId());
	}

	public Like save(Like like) {
		return likeRepository.save(like);
	}

	public Optional<Like> findByUserIdAndProductId(Long userId, Long productId) {
		return likeRepository.findByUserIdAndProductId(userId, productId);
	}

}
