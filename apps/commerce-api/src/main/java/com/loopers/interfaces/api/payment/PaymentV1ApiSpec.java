package com.loopers.interfaces.api.payment;

import com.loopers.domain.payment.PaymentCallbackRequest;
import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Payment V1 API", description = "결제 API 입니다.")
public interface PaymentV1ApiSpec {

	@PostMapping("/api/v1/payments")
	@Operation(summary = "결제요청 API")
	ApiResponse<Object> requestPayment(
			@Schema(name = "X-USER-ID")
			@RequestHeader(name = "X-USER-ID") String userId,
			@RequestBody PaymentV1Dto.PaymentRequest paymentRequest
			);

	@PostMapping("/api/v1/payments/callback")
	@Operation(summary = "콜백 API")
	ApiResponse<Object> requestPaymentCallback(@RequestBody PaymentCallbackRequest/*String*/ paymentCallbackRequest);
}
