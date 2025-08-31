package com.loopers.application.dataplatform;

import com.loopers.domain.dataplatform.DataPlatformDto;
import com.loopers.domain.dataplatform.DataPlatformPort;
import com.loopers.domain.dataplatform.DataResultStatus;
import com.loopers.domain.order.event.OrderCreatedEvent;
import com.loopers.interfaces.event.dataplatform.PaymentOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataPlatformEventHandler {
	private final DataPlatformPort dataPlatformPort;

	public void forwardOrderCreatedData(OrderCreatedEvent event) {
		DataPlatformDto.Result result = dataPlatformPort.sendOrderCreatedData(event);

		if (result.status() == DataResultStatus.SUCCESS)
			log.info("[데이터 플랫폼] 주문 생성 정보 전송 성공 - UserId: {}, OrderId: {}, Message: {}",
					event.userId(),
					event.orderId(),
					result.message());

		if (result.status() == DataResultStatus.FAILURE)
			log.error("[데이터 플랫폼] 주문 생성 정보 전송 실패 - UserId: {}, OrderId:{}, Message: {}",
					event.userId(),
					event.orderId(),
					result.message());
	}

	public void forwardOrderSuccessResultData(PaymentOrderDto.PaymentOrderEventDto event) {
		DataPlatformDto.Result result = dataPlatformPort.sendOrderResultData(event);

		if (result.status() == DataResultStatus.SUCCESS)
			log.info("[데이터 플랫폼] 주문 성공 정보 전송 성공 - UserId: {}, OrderId: {}, PaymentId: {}, Message: {}",
					event.userId(),
					event.orderId(),
					event.paymentId(),
					result.message());

		if (result.status() == DataResultStatus.FAILURE)
			log.error("[데이터 플랫폼] 주문 성공 정보 전송 실패 - UserId: {}, OrderId: {}, PaymentId: {}, Message: {}",
					event.userId(),
					event.orderId(),
					event.paymentId(),
					result.message());
	}

	public void forwardOrderFailResultData(PaymentOrderDto.PaymentOrderEventDto event) {
		DataPlatformDto.Result result = dataPlatformPort.sendOrderResultData(event);

		if (result.status() == DataResultStatus.SUCCESS)
			log.info("[데이터 플랫폼] 주문 실패 정보 전송 성공 - UserId: {}, OrderId: {}, PaymentId: {}, Message: {}",
					event.userId(),
					event.orderId(),
					event.paymentId(),
					result.message());

		if (result.status() == DataResultStatus.FAILURE)
			log.error("[데이터 플랫폼] 주문 실패 정보 전송 실패 - UserId: {}, OrderId: {}, PaymentId: {}, Message: {}",
					event.userId(),
					event.orderId(),
					event.paymentId(),
					result.message());
	}

	public void forwardPaymentOrderSuccessResultData(PaymentOrderDto.PaymentOrderEventDto event){
		DataPlatformDto.Result result = dataPlatformPort.sendPaymentOrderResultData(event);

		if (result.status() == DataResultStatus.SUCCESS)
			log.info("[데이터 플랫폼] 결제-주문 성공 정보 전송 성공 - UserId: {}, OrderId: {}, PaymentId: {}, Message: {}",
					event.userId(),
					event.orderId(),
					event.paymentId(),
					result.message());

		if (result.status() == DataResultStatus.FAILURE)
			log.error("[데이터 플랫폼] 결제-주문 성공 정보 전송 실패 - UserId: {}, OrderId: {}, PaymentId: {}, Message: {}",
					event.userId(),
					event.orderId(),
					event.paymentId(),
					result.message());
	}

	public void forwardPaymentOrderFailResultData(PaymentOrderDto.PaymentOrderEventDto event){
		DataPlatformDto.Result result = dataPlatformPort.sendPaymentOrderResultData(event);

		if (result.status() == DataResultStatus.SUCCESS)
			log.info("[데이터 플랫폼] 결제-주문 실패 정보 전송 성공 - UserId: {}, OrderId: {}, PaymentId: {}, Message: {}",
					event.userId(),
					event.orderId(),
					event.paymentId(),
					result.message());

		if (result.status() == DataResultStatus.FAILURE)
			log.error("[데이터 플랫폼] 결제-주문 실패 정보 전송 실패 - UserId: {}, OrderId: {}, PaymentId: {}, Message: {}",
					event.userId(),
					event.orderId(),
					event.paymentId(),
					result.message());
	}
}
