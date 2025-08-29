package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentCommand;
import com.loopers.application.payment.PaymentInfo;
import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.domain.payment.PaymentType;

public class PaymentV1Dto {
	public record PaymentRequest(
			Long orderId,
			PaymentType paymentType,
			CardType cardType,
			String cardNo,
			Integer amount
	) {
		public PaymentCommand toCommand(String userId) {
			return new PaymentCommand(userId, orderId, paymentType, cardType, cardNo, amount);
		}
	}

	public record PaymentResponse(
			Long paymentId,
			String transactionKey,
			String userId,
			Long orderId,
			int amount,
			PaymentType paymentType,
			PaymentStatus paymentStatus
	) {
		public static PaymentResponse from(PaymentInfo paymentInfo) {
			return new PaymentResponse(
					paymentInfo.paymentId(),
					paymentInfo.transactionKey(),
					paymentInfo.userId(),
					paymentInfo.orderId(),
					paymentInfo.amount(),
					paymentInfo.paymentType(),
					paymentInfo.paymentStatus()
			);
		}
	}
}
