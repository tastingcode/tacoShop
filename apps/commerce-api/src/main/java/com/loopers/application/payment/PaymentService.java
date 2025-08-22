package com.loopers.application.payment;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderRepository;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.*;
import com.loopers.domain.user.UserDomainService;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
	private final UserRepository userRepository;
	private final UserDomainService userDomainService;
	private final PaymentGatewayPort paymentGatewayPort;
	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;

	public void checkOut(PaymentCommand command) {
		// 일반 유저 조회
		UserEntity user = getValidatedUser(command.userId());

		// command.paymentType() == PaymentType.CREDIT_CARD
		PaymentInfo.PaymentRequest paymentRequest = PaymentInfo.PaymentRequest.of(command.orderId(),
				command.paymentType(),
				command.cardType(),
				command.cardNo(),
				command.amount());

		try {
			PaymentInfo.PaymentResponse paymentResponse = paymentGatewayPort.requestPayment(paymentRequest);
			Payment payment = Payment.of(paymentResponse.data().transactionKey(),
					command.orderId(),
					command.amount(),
					command.paymentType(),
					PaymentStatus.PENDING);
			paymentRepository.save(payment);
		} catch (Exception e) {
			log.error("주문 ID = {} 에 해당하는 결제 요청 실패, 에러 메시지 {}", command.orderId(), e.getMessage());
		}

	}

	@Transactional
	public void callBack(PaymentCallbackRequest paymentCallbackRequest) {
		// 결제 트랜잭션 유효성 검증
		validatePaymentTransaction(paymentCallbackRequest.transactionKey());

		// 주문 유효성 검증
		Order order = getValidatedOrder(paymentCallbackRequest.orderId());

		// 결제 유효성 검증
		Payment payment = getValidatedPayment(paymentCallbackRequest.transactionKey());

		// 결제 상태 업데이트
		payment.updateStatus(paymentCallbackRequest.status());

		// 주문 상태 업데이트
		updateOrderStatus(paymentCallbackRequest, order);
	}



	private static void updateOrderStatus(PaymentCallbackRequest paymentCallbackRequest, Order order) {
		if (paymentCallbackRequest.status().equals(PaymentStatus.SUCCESS)) {
			order.updateStatus(OrderStatus.COMPLETED);
		}
		if (paymentCallbackRequest.status().equals(PaymentStatus.FAILED)) {
			order.updateStatus(OrderStatus.FAILURE);
		}
	}

	public void validatePaymentTransaction(String transactionKey) {
		PaymentInfo.PaymentResponse paymentResponse = paymentGatewayPort.requestPaymentInfo(transactionKey);
		if (paymentResponse.data() == null)
			throw new CoreException(ErrorType.NOT_FOUND, "해당 트랜잭션의 결제 정보를 확인할 수 없습니다.");
	}

	private UserEntity getValidatedUser(String userId) {
		UserEntity user = userDomainService.getUser(userId);
		if (user == null) {
			throw new CoreException(ErrorType.NOT_FOUND, "로그인 한 회원만 이용할 수 있습니다.");
		}
		return user;
	}

	private Order getValidatedOrder(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(null);
		if (order == null) {
			throw new CoreException(ErrorType.NOT_FOUND, "해당 주문 이력을 찾을 수 없습니다.");
		}
		return order;
	}

	private Payment getValidatedPayment(String transactionKey) {
		Payment payment = paymentRepository.findByTransactionKey(transactionKey).orElse(null);
		if (payment == null) {
			throw new CoreException(ErrorType.NOT_FOUND, "해당 주문의 결제 이력을 찾을 수 없습니다.");
		}
		return payment;
	}


}
