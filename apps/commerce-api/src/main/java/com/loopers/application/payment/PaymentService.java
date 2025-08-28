package com.loopers.application.payment;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderDomainService;
import com.loopers.domain.payment.*;
import com.loopers.domain.payment.event.PaymentOrderFailEvent;
import com.loopers.domain.payment.event.PaymentOrderSuccessEvent;
import com.loopers.domain.user.UserDomainService;
import com.loopers.domain.user.UserEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
	private final UserDomainService userDomainService;
	private final PaymentRepository paymentRepository;
	private final OrderDomainService orderDomainService;
	private final PaymentDomainService paymentDomainService;
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	public Payment createPayment(String transactionKey, PaymentCommand command, PaymentStatus paymentStatus) {
		Payment payment = Payment.of(transactionKey,
				command.userId(),
				command.orderId(),
				command.amount(),
				command.paymentType(),
				paymentStatus);
		return paymentRepository.save(payment);
	}

	@Transactional
	public void syncPaymentOrder(Payment payment, PaymentStatus paymentStatus) {
		// 주문 조회
		Order order = orderDomainService.getOrder(payment.getOrderId());

		// 결제 성공
		if (paymentStatus.equals(PaymentStatus.SUCCESS))
			eventPublisher.publishEvent(PaymentOrderSuccessEvent.of(payment.getUserId(), order.getId()));

		// 결제 실패
		if (paymentStatus.equals(PaymentStatus.SUCCESS))
			eventPublisher.publishEvent(PaymentOrderFailEvent.of(payment.getUserId(), order.getId()));

		// 결제 상태 업데이트 (SUCCESS OR FAILED)
		payment.updateStatus(paymentStatus);
	}

	@Transactional(readOnly = true)
	public Payment getValidatedPayment(PaymentCallbackRequest paymentCallbackRequest) {
		Order order = orderDomainService.getOrder(paymentCallbackRequest.orderId());
		Payment payment = paymentDomainService.getPayment(paymentCallbackRequest.transactionKey());

		if (!order.getId().equals(payment.getOrderId())) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문과 결제 정보가 일치하지 않습니다.");
		}
		return payment;
	}

	@Transactional(readOnly = true)
	public void validateUserOrder(PaymentCommand paymentCommand) {
		UserEntity user = userDomainService.getUserByUserId(paymentCommand.userId());
		Order order = orderDomainService.getOrder(paymentCommand.orderId());
		if (!user.getId().equals(order.getUserId())) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문과 사용자 정보가 일치하지 않습니다.");
		}
	}

}
