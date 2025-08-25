package com.loopers.domain.payment;

import com.loopers.infrastructure.payment.pg.PgDto;

public class PaymentInfo {
	public record PaymentRequest(
			Long orderId,
			PaymentType paymentType,
			CardType cardType,
			String cardNo,
			int finalPrice
	) {
		public static PaymentRequest of(Long orderId,
										PaymentType paymentType,
										CardType cardType,
										String cardNo,
										int finalPrice) {
			return new PaymentRequest(orderId, paymentType, cardType, cardNo, finalPrice);
		}
	}

	public record PaymentResponse(
			Meta meta,
			Data data
	) {
		public record Meta(
				String result,
				String errorCode,
				String message
		) {
			public static Meta of(PgDto.PgResponse pgResponse) {
				return new Meta(pgResponse.meta().result(),
						pgResponse.meta().errorCode(),
						pgResponse.meta().message());
			}
		}

		public record Data(
				String transactionKey,
				String status,
				String reason
		) {
			public static Data of(PgDto.PgResponse pgResponse) {
				return new Data(pgResponse.data().transactionKey(),
						pgResponse.data().status(),
						pgResponse.data().reason());
			}
		}

		public static PaymentResponse of(PgDto.PgResponse pgResponse) {
			return new PaymentResponse(Meta.of(pgResponse), Data.of(pgResponse));
		}

		public static PaymentResponse fallback(String message){
			Meta meta = new Meta("fail", null, message);
			Data data = new Data(null, null, null);
			return new PaymentResponse(meta, data);
		}
	}
}
