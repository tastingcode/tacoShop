package com.loopers.infrastructure.like;

import com.loopers.domain.brand.QBrand;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeRepository;
import com.loopers.domain.like.QLike;
import com.loopers.domain.product.ProductDetail;
import com.loopers.domain.product.QProduct;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class LikeRepositoryImpl implements LikeRepository {
	private final LikeJpaRepository likeJpaRepository;
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Like save(Like like) {
		return likeJpaRepository.save(like);
	}

	@Override
	public Optional<Like> findByUserIdAndProductId(Long userId, Long productId) {
		return likeJpaRepository.findByUserIdAndProductId(userId, productId);
	}

	@Override
	public List<ProductDetail> findLikedProducts(Long userId) {
		QLike like = QLike.like;
		QProduct product = QProduct.product;
		QBrand brand = QBrand.brand;

		List<ProductDetail> likedProductDetails = jpaQueryFactory
				.select(Projections.constructor(ProductDetail.class,
						product.id,
						product.name,
						product.price,
						product.stock,
						product.likeCount,
						brand.id,
						brand.name,
						brand.description
				))
				.from(like)
				.join(product).on(like.productId.eq(product.id))
				.join(brand).on(product.brandId.eq(brand.id))
				.where(
						like.userId.eq(userId),
						like.liked.eq(true)
				)
				.fetch();

		return likedProductDetails;
	}
}
