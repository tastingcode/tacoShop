package com.loopers.application.like;

import com.loopers.application.product.ProductInfo;
import com.loopers.domain.like.LikeDomainService;
import com.loopers.domain.like.LikeInfoDto;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDetail;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LikeService {
	private final UserService userService;
	private final ProductService productService;
	private final LikeDomainService likeDomainService;

	@Retryable(
			retryFor = ObjectOptimisticLockingFailureException.class,
			maxAttempts = 5,
			backoff = @Backoff(delay = 100))
	@Transactional
	public LikeInfo like(String userId, Long productId) {
		UserEntity user = getVerifiedUser(userId);
		Product product = getVerifiedProduct(productId);

		LikeInfoDto likeInfoDto = likeDomainService.like(user, product);
		return LikeInfo.from(likeInfoDto);
	}

	@Retryable(
			retryFor = ObjectOptimisticLockingFailureException.class,
			maxAttempts = 5,
			backoff = @Backoff(delay = 100))
	@Transactional
	public LikeInfo unLike(String userId, Long productId) {
		UserEntity user = getVerifiedUser(userId);
		Product product = getVerifiedProduct(productId);

		LikeInfoDto likeInfoDto = likeDomainService.unLike(user, product);
		return LikeInfo.from(likeInfoDto);
	}

	@Transactional(readOnly = true)
	public List<ProductInfo> getLikedProducts(String userId) {
		UserEntity user = getVerifiedUser(userId);

		List<ProductDetail> likedProducts = likeDomainService.getLikedProducts(user);
		List<ProductInfo> productInfoList = likedProducts.stream()
				.map(ProductInfo::from)
				.toList();

		return productInfoList;
	}

	private UserEntity getVerifiedUser(String userId) {
		UserEntity user = userService.getUser(userId);
		if (user == null) {
			throw new CoreException(ErrorType.NOT_FOUND, "로그인 한 회원만 이용할 수 있습니다.");
		}
		return user;
	}

	private Product getVerifiedProduct(Long productId) {
		return productService.getProduct(productId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));
	}
}
