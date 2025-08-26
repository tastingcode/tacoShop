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

	public List<ProductDetail> getLikedProducts(UserEntity user) {
		return likeRepository.findLikedProducts(user.getId());
	}

	public Like getLikeOrCreate(Optional<Like> optionalLike, UserEntity user, Product product) {
		return optionalLike.orElseGet(() -> Like.of(user.getId(), product.getId()));
	}

	public Like save(Like like) {
		return likeRepository.save(like);
	}

}
