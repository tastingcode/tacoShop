package com.loopers.domain.payment;

public record PaymentCallbackRequest(
		String transactionKey,
		Long orderId,
		CardType cardType,
		String cardNo,
		int amount,
		PaymentStatus status,
		String reason
) {
}
