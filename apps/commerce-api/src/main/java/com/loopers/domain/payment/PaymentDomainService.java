package com.loopers.domain.payment;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentDomainService {
	private final PaymentRepository paymentRepository;

	public Payment getPayment(String transactionKey) {
		return paymentRepository.findByTransactionKey(transactionKey)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 주문의 결제 이력을 찾을 수 없습니다."));
	}

}
