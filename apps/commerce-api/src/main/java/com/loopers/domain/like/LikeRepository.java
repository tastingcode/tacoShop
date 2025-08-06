package com.loopers.domain.like;

import com.loopers.domain.product.ProductDetail;

import java.util.List;
import java.util.Optional;

public interface LikeRepository {
	Like save(Like like);

	Optional<Like> findByUserIdAndProductId(Long userId, Long productId);

	List<ProductDetail> findLikedProducts(Long userId);
}


