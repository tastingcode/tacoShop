package com.loopers.application.payment;

import com.loopers.application.point.PointService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.domain.payment.PaymentStrategy;
import com.loopers.domain.payment.event.PaymentOrderFailEvent;
import com.loopers.domain.payment.event.PaymentOrderSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointPaymentStrategy implements PaymentStrategy {

	private final PaymentService paymentService;
	private final PointService pointService;
	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public PaymentInfo pay(PaymentCommand command) {
		// 결제 성공 여부
		boolean isSuccessPaymentOrder = false;

		try {
			// 포인트 사용
			pointService.useMyPoint(command.userId(), command.amount());

			// 결제 성공
			isSuccessPaymentOrder = true;
		}catch (Exception e) {
			log.error("포인트 결제 중 에러가 발생하였습니다.: {}", e.getMessage());
		}

		// 결제 생성
		Payment payment;
		if (isSuccessPaymentOrder) {
			payment = paymentService.createPayment(null, command, PaymentStatus.SUCCESS);
			// 결제 성공 이벤트 발행
			eventPublisher.publishEvent(PaymentOrderSuccessEvent.of(payment.getId(), command.userId(), command.orderId()));
		}
		else {
			payment = paymentService.createPayment(null, command, PaymentStatus.FAILED);
			// 결제 실패 이벤트 발행
			eventPublisher.publishEvent(PaymentOrderFailEvent.of(payment.getId(), command.userId(), command.orderId()));
		}

		return PaymentInfo.from(payment);
	}
}
