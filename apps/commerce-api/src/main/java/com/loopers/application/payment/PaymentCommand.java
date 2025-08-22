package com.loopers.application.payment;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentType;

public record PaymentCommand(
		String userId,
		Long orderId,
		PaymentType paymentType,
		CardType cardType,
		String cardNo,
		int amount
) {
	public static PaymentCommand from(String userId, Long orderId, PaymentType paymentType, CardType cardType, String cardNo, Integer amount) {
		// TODO validate
		return new PaymentCommand(userId, orderId, paymentType, cardType, cardNo, amount);
	}
}
