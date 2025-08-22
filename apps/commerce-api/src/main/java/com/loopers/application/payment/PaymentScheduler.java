package com.loopers.application.payment;


import com.loopers.domain.payment.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentScheduler {
	private final PaymentRepository paymentRepository;
	private final PaymentGatewayPort paymentGatewayPort;

	@Scheduled(fixedDelay = 600000)
	@Transactional
	public void syncPaymentStatus() {
		List<Payment> pendingPayments = paymentRepository.findByPaymentStatusAndCreatedAtBefore(
				PaymentStatus.PENDING,
				ZonedDateTime.now().minusMinutes(5)
		);
		for (Payment pendingPayment : pendingPayments) {
			PaymentInfo.PaymentResponse paymentResponse = paymentGatewayPort.requestPaymentInfo(pendingPayment.getTransactionKey());
			PaymentStatus currentPaymentStatus = PaymentStatus.valueOf(paymentResponse.data().status());

			if (currentPaymentStatus != PaymentStatus.PENDING)
				pendingPayment.updateStatus(currentPaymentStatus);
		}
	}

}
