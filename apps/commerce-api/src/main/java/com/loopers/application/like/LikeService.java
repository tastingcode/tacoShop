package com.loopers.application.like;

import com.loopers.application.product.ProductInfo;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeDomainService;
import com.loopers.domain.like.event.LikeEventType;
import com.loopers.domain.like.event.ProductLikeEvent;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDetail;
import com.loopers.domain.product.ProductDomainService;
import com.loopers.domain.user.UserDomainService;
import com.loopers.domain.user.UserEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeService {
	private final UserDomainService userDomainService;
	private final ProductDomainService productDomainService;
	private final LikeDomainService likeDomainService;
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	public LikeInfo like(String userId, Long productId) {
		UserEntity user = getValidatedUser(userId);
		Product product = getValidatedProduct(productId);

		// 좋아요 조회
		Optional<Like> optionalLike = likeDomainService.getLike(user, product);
		Like like = likeDomainService.getLikeOrCreate(optionalLike, user, product);

		// 좋아요 상태 false 일 때 좋아요 이벤트 발생
		if (!like.isLiked())
			eventPublisher.publishEvent(ProductLikeEvent.of(like, LikeEventType.LIKE));

		// 좋아요 처리
		like.like();

		return LikeInfo.from(like);
	}

	@Transactional
	public LikeInfo unlike(String userId, Long productId) {
		UserEntity user = getValidatedUser(userId);
		Product product = getValidatedProduct(productId);

		// 좋아요 조회
		Like like = likeDomainService.getLike(user, product)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "좋아요 한 상품만 좋아요를 취소 할 수 있습니다."));

		// 좋아요 상태 true 일 때 좋아요 이벤트 발생
		if (like.isLiked())
			eventPublisher.publishEvent(ProductLikeEvent.of(like, LikeEventType.UNLIKE));

		// 좋아요 취소 처리
		like.unLike();

		return LikeInfo.from(like);
	}

	@Transactional(readOnly = true)
	public List<ProductInfo> getLikedProducts(String userId) {
		UserEntity user = getValidatedUser(userId);

		List<ProductDetail> likedProducts = likeDomainService.getLikedProducts(user);
		List<ProductInfo> productInfoList = likedProducts.stream()
				.map(ProductInfo::from)
				.toList();

		return productInfoList;
	}

	private UserEntity getValidatedUser(String userId) {
		UserEntity user = userDomainService.getUserByUserId(userId);
		if (user == null) {
			throw new CoreException(ErrorType.NOT_FOUND, "로그인 한 회원만 이용할 수 있습니다.");
		}
		return user;
	}

	private Product getValidatedProduct(Long productId) {
		return productDomainService.getProduct(productId);
	}
}
