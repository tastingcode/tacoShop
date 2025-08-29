package com.loopers.domain.like;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDetail;
import com.loopers.domain.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class LikeDomainService {
	private final LikeRepository likeRepository;

	public Optional<Like> getLike(UserEntity user, Product product) {
		return likeRepository.findByUserIdAndProductId(user.getId(), product.getId());
	}

	public Like getLikeOrCreate(Optional<Like> optionalLike, UserEntity user, Product product) {
		if (optionalLike.isPresent()) {
			return optionalLike.get();
		}

		Like like = Like.of(user.getId(), product.getId());
		return likeRepository.save(like);
	}

	public List<ProductDetail> getLikedProducts(UserEntity user) {
		return likeRepository.findLikedProducts(user.getId());
	}

}
