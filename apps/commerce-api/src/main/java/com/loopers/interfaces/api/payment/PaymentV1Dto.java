package com.loopers.interfaces.api.payment;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentType;

public class PaymentV1Dto {
	public record PaymentRequest(
			Long orderId,
			PaymentType paymentType,
			CardType cardType,
			String cardNo,
			Integer amount
	) {
	}
}
