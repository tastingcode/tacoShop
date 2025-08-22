package com.loopers.infrastructure.payment.pg;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentInfo;

public class PgDto {

	public record PgRequest(
			String orderId,
			CardType cardType,
			String cardNo,
			@JsonProperty("amount") int finalPrice,
			String callbackUrl
	){
		public static PgRequest from(PaymentInfo.PaymentRequest paymentRequest, String callbackUrl) {
			return new PgRequest(String.valueOf(paymentRequest.orderId()),
					paymentRequest.cardType(),
					paymentRequest.cardNo(),
					paymentRequest.finalPrice(),
					callbackUrl);
		}
	}

	public record PgResponse(
			Meta meta,
			Data data
	) {
		public record Meta(
				String result,
				String errorCode,
				String message
		) {}

		public record Data(
				String transactionKey,
				String status,
				String reason
		) {}
	}

}
