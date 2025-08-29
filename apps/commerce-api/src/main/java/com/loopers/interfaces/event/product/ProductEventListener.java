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
		productLikeEventHandler.updateLikeCount(event);
	}
}
