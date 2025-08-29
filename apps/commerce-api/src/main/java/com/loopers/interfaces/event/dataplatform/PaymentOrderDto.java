package com.loopers.interfaces.event.dataplatform;

import com.loopers.domain.payment.event.PaymentOrderFailEvent;
import com.loopers.domain.payment.event.PaymentOrderSuccessEvent;

public class PaymentOrderDto {
	public record PaymentOrderEventDto(
			String userId,
			Long orderId,
			Long paymentId
	) {
		public static PaymentOrderEventDto from(PaymentOrderSuccessEvent paymentOrderSuccessEvent) {
			return new PaymentOrderEventDto(paymentOrderSuccessEvent.userId(),
					paymentOrderSuccessEvent.orderId(),
					paymentOrderSuccessEvent.paymentId());
		}

		public static PaymentOrderEventDto from(PaymentOrderFailEvent paymentOrderFailEvent) {
			return new PaymentOrderEventDto(paymentOrderFailEvent.userId(),
					paymentOrderFailEvent.orderId(),
					paymentOrderFailEvent.paymentId());
		}
	}
}
