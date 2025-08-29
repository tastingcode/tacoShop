package com.loopers.application.payment;

import com.loopers.domain.payment.PaymentStrategy;
import com.loopers.domain.payment.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyFactory {
	private final PointPaymentStrategy pointPaymentStrategy;
	private final CardPaymentStrategy cardPaymentStrategy;

	public PaymentStrategy getPaymentStrategy(PaymentType paymentType) {
		return switch (paymentType) {
			case PaymentType.POINT -> pointPaymentStrategy;
			case PaymentType.CREDIT_CARD -> cardPaymentStrategy;
		};
	}
}
