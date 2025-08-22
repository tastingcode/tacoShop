package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
	Optional<Payment> findByOrderId(Long orderId);

	Optional<Payment> findByTransactionKey(String transactionKey);

	List<Payment> findByPaymentStatusAndCreatedAtBefore(PaymentStatus paymentStatus, ZonedDateTime createdAt);
}
