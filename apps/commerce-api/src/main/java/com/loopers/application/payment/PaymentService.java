package com.loopers.application.payment;

import com.loopers.application.coupon.CouponService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderDomainService;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.*;
import com.loopers.domain.user.UserDomainService;
import com.loopers.domain.user.UserEntity;
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
	private final UserDomainService userDomainService;
	private final PaymentRepository paymentRepository;
	private final OrderDomainService orderDomainService;
	private final PaymentDomainService paymentDomainService;
	private final CouponService couponService;


	@Transactional
	public void createPayment(PaymentInfo.PaymentResponse paymentResponse, PaymentCommand command) {
		Payment payment = Payment.of(paymentResponse.data().transactionKey(),
				command.orderId(),
				command.amount(),
				command.paymentType(),
				PaymentStatus.PENDING);
		paymentRepository.save(payment);
	}


	@Transactional
	public void updatePaymentAndOrder(Payment payment, PaymentStatus paymentStatus) {
		// 결제 상태 업데이트
		payment.updateStatus(paymentStatus);

		Order order = orderDomainService.getOrder(payment.getOrderId());

		// 주문 상태 업데이트
		syncOrderStatus(order, paymentStatus);
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
	public void validateOrder(PaymentCommand paymentCommand){
		UserEntity user = userDomainService.getUserByUserId(paymentCommand.userId());
		Order order = orderDomainService.getOrder(paymentCommand.orderId());
		if (!user.getId().equals(order.getUserId())) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문과 사용자 정보가 일치하지 않습니다.");
		}
	}

	public void syncOrderStatus(Order order, PaymentStatus paymentStatus) {
		// 결제 성공
		if (paymentStatus.equals(PaymentStatus.SUCCESS)) {
			order.updateStatus(OrderStatus.COMPLETED);
		}

		// 결제 실패
		if (paymentStatus.equals(PaymentStatus.FAILED)) {
			// 재고 복구
			orderDomainService.restoreStocks(order.getOrderProducts());
			// 쿠폰 복구
			couponService.restoreCoupon(order.getUserId(), order.getCouponId());

			// 주문 실패 처리
			order.updateStatus(OrderStatus.FAILURE);
		}
	}

}
