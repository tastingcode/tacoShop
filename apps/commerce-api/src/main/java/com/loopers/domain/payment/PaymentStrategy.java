package com.loopers.domain.payment;

import com.loopers.application.payment.PaymentCommand;
import com.loopers.application.payment.PaymentInfo;

public interface PaymentStrategy {
	PaymentInfo pay(PaymentCommand command);
}
