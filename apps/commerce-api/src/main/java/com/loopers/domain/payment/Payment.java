package com.loopers.domain.payment;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Payment extends BaseEntity {
	private String transactionKey;
	private Long orderId;
	private int amount;
	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;

	public static Payment of(String transactionKey,
							 Long orderId,
							 int amount,
							 PaymentType paymentType,
							 PaymentStatus paymentStatus) {
		Payment payment = new Payment();
		payment.transactionKey = transactionKey;
		payment.orderId = orderId;
		payment.amount = amount;
		payment.paymentType = paymentType;
		payment.paymentStatus = paymentStatus;
		return payment;
	}

	public void updateStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
}
