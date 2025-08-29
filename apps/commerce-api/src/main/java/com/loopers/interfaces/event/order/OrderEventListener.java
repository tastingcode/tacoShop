package com.loopers.interfaces.event.order;

import com.loopers.application.order.event.OrderEventHandler;
import com.loopers.domain.payment.event.PaymentOrderFailEvent;
import com.loopers.domain.payment.event.PaymentOrderSuccessEvent;
import com.loopers.domain.payment.event.PaymentRequestFailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderEventListener {
	private final OrderEventHandler orderEventHandler;

	@Async
	@EventListener
	public void handlePaymentRequestFailEvent(PaymentRequestFailEvent event){
		orderEventHandler.failOrder(event.orderId());
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void handlePaymentOrderSuccessEvent(PaymentOrderSuccessEvent event){
		orderEventHandler.completeOrder(event);
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void handlePaymentOrderFailEvent(PaymentOrderFailEvent event){
		orderEventHandler.failOrder(event.orderId());
	}

}
