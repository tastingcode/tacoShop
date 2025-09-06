package com.loopers.interfaces.event.product;

import com.loopers.application.product.event.ProductLikeEventHandler;
import com.loopers.domain.like.event.ProductLikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProductEventListener {

	private final ProductLikeEventHandler productLikeEventHandler;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleProductLikeEvent(ProductLikeEvent event){
		// 좋아요 수 업데이트
		productLikeEventHandler.updateLikeCount(event);

		// 좋아요 수 캐시 무효화
		productLikeEventHandler.likeChanged();
	}
}
