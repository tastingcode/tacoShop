package com.loopers.interfaces.event.coupon;

import com.loopers.application.coupon.event.CouponEventHandler;
import com.loopers.domain.order.event.OrderCreatedEvent;
import com.loopers.domain.payment.event.PaymentOrderFailEvent;
import com.loopers.domain.payment.event.PaymentRequestFailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CouponEventListener {

	private final CouponEventHandler couponEventHandler;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleOrderCreatedEvent(OrderCreatedEvent event) {
		couponEventHandler.useCoupon(event);
	}

	@Async
	@EventListener
	public void handlePaymentRequestFailEvent(PaymentRequestFailEvent event) {
		couponEventHandler.restoreCoupon(event.userId(), event.orderId());
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void handlePaymentOrderFailEvent(PaymentOrderFailEvent event) {
		couponEventHandler.restoreCoupon(event.userId(), event.orderId());
	}

}
