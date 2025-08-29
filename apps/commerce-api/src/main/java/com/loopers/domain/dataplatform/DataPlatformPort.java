package com.loopers.domain.dataplatform;

import com.loopers.domain.order.event.OrderCreatedEvent;
import com.loopers.interfaces.event.dataplatform.PaymentOrderDto;

public interface DataPlatformPort {
	DataPlatformDto.Result sendOrderCreatedData(OrderCreatedEvent event);
	DataPlatformDto.Result sendOrderResultData(PaymentOrderDto.PaymentOrderEventDto event);
	DataPlatformDto.Result sendPaymentOrderResultData(PaymentOrderDto.PaymentOrderEventDto event);
}
