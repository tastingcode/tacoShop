package com.loopers.infrastructure.dataplatform;

import com.loopers.domain.dataplatform.DataPlatformDto;
import com.loopers.domain.dataplatform.DataPlatformPort;
import com.loopers.domain.order.event.OrderCreatedEvent;
import com.loopers.interfaces.event.dataplatform.PaymentOrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataPlatformAdapter implements DataPlatformPort {

	@Override
	public DataPlatformDto.Result sendOrderCreatedData(OrderCreatedEvent event) {
		try {
			log.info("[데이터 플랫폼] 주문 생성 정보 전송 -  UserId: {}, OrderId: {}, finalPrice: {}, discountAmount: {}, couponId: {}",
					event.userId(),
					event.orderId(),
					event.finalPrice(),
					event.discountAmount(),
					event.couponId());

			return DataPlatformDto.Result.success(event.userId(), "주문 생성 정보 전송 성공");
		} catch (Exception e) {
			log.error("[데이터 플랫폼] 서버가 불안정하여 주문 정보 전송에 실패했습니다.{}", e.getMessage());
			return DataPlatformDto.Result.fail(event.userId(), "주문 생성 정보 전송 실패");
		}
	}

	@Override
	public DataPlatformDto.Result sendOrderResultData(PaymentOrderDto.PaymentOrderEventDto event) {
		try {
			log.info("[데이터 플랫폼] 주문 결과 정보 전송 - UserId: {}, OrderId: {}, PaymentId: {}",
					event.userId(),
					event.orderId(),
					event.paymentId());

			return DataPlatformDto.Result.success(event.userId(), "주문 결과 정보 전송 성공");
		} catch (Exception e) {
			log.error("[데이터 플랫폼] 주문 결과 정보 전송 실패: {}", e.getMessage());
			return DataPlatformDto.Result.fail(event.userId(), "주문 결과 정보 전송 실패");
		}
	}

	@Override
	public DataPlatformDto.Result sendPaymentOrderResultData(PaymentOrderDto.PaymentOrderEventDto event) {
		try {
			log.info("[데이터 플랫폼] 결제-주문 결과 정보 전송 - UserId: {}, OrderId: {}, PaymentId: {}",
					event.userId(),
					event.orderId(),
					event.paymentId());

			return DataPlatformDto.Result.success(event.userId(), "결제-주문 결과 정보 전송 성공");
		} catch (Exception e) {
			log.error("[데이터 플랫폼] 결제-주문 결과 정보 전송 실패: {}", e.getMessage());
			return DataPlatformDto.Result.fail(event.userId(), "결제-주문 결과 정보 전송 실패");
		}
	}
}
