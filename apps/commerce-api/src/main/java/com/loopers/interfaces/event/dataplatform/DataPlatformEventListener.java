package com.loopers.interfaces.event.dataplatform;

import com.loopers.application.dataplatform.DataPlatformEventHandler;
import com.loopers.domain.order.event.OrderCreatedEvent;
import com.loopers.domain.payment.event.PaymentOrderFailEvent;
import com.loopers.domain.payment.event.PaymentOrderSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DataPlatformEventListener {
	private final DataPlatformEventHandler dataPlatformEventHandler;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleOrderCreatedEvent(OrderCreatedEvent event){
		dataPlatformEventHandler.forwardOrderCreatedData(event);
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handlePaymentOrderSuccessEvent(PaymentOrderSuccessEvent event){
		PaymentOrderDto.PaymentOrderEventDto paymentOrderEventDto = PaymentOrderDto.PaymentOrderEventDto.from(event);
		dataPlatformEventHandler.forwardOrderSuccessResultData(paymentOrderEventDto);
		dataPlatformEventHandler.forwardPaymentOrderSuccessResultData(paymentOrderEventDto);
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handlePaymentOrderFailEvent(PaymentOrderFailEvent event){
		PaymentOrderDto.PaymentOrderEventDto paymentOrderEventDto = PaymentOrderDto.PaymentOrderEventDto.from(event);
		dataPlatformEventHandler.forwardOrderFailResultData(paymentOrderEventDto);
		dataPlatformEventHandler.forwardPaymentOrderFailResultData(paymentOrderEventDto);
	}

}
