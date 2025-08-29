package com.loopers.application.product.event;

import com.loopers.domain.like.event.LikeEventType;
import com.loopers.domain.like.event.ProductLikeEvent;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDomainService;
import com.loopers.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProductLikeEventHandler {

	private final ProductDomainService productDomainService;
	private final ProductRepository productRepository;

	@Retryable(
			retryFor = ObjectOptimisticLockingFailureException.class,
			maxAttempts = 5,
			backoff = @Backoff(delay = 100))
	@Transactional
	public void updateLikeCount(ProductLikeEvent event) {
		Product product = productDomainService.getProduct(event.productId());

		if (event.likeEventType() == LikeEventType.LIKE)
			product.increaseLikeCount();

		if (event.likeEventType() == LikeEventType.UNLIKE)
			product.decreaseLikeCount();

		productRepository.save(product);
	}

}
