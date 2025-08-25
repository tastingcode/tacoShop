package com.loopers.application.payment;

import com.loopers.domain.payment.PaymentGatewayPort;
import com.loopers.domain.payment.PaymentInfo;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentGatewayService {

	private final PaymentGatewayPort paymentGatewayPort;

	public PaymentInfo.PaymentResponse requestPayment(PaymentCommand command) {
		PaymentInfo.PaymentRequest paymentRequest = PaymentInfo.PaymentRequest.of(command.orderId(),
				command.paymentType(),
				command.cardType(),
				command.cardNo(),
				command.amount());

		return paymentGatewayPort.requestPayment(paymentRequest);
	}

	public void validatePaymentTransaction(String transactionKey) {
		PaymentInfo.PaymentResponse paymentResponse = paymentGatewayPort.requestPaymentInfo(transactionKey);
		if (paymentResponse.data() == null)
			throw new CoreException(ErrorType.NOT_FOUND, "해당 트랜잭션의 결제 정보를 확인할 수 없습니다.");
	}
}
