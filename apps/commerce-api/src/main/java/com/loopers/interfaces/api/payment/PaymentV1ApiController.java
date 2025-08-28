package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentCommand;
import com.loopers.application.payment.PaymentFacade;
import com.loopers.application.payment.PaymentInfo;
import com.loopers.application.payment.PaymentService;
import com.loopers.domain.payment.PaymentCallbackRequest;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentV1ApiController implements PaymentV1ApiSpec {

	private final PaymentFacade paymentFacade;

	@PostMapping
	@Override
	public ApiResponse<PaymentV1Dto.PaymentResponse> requestPayment(@RequestHeader("X-USER-ID") String userId,
											  @RequestBody PaymentV1Dto.PaymentRequest paymentRequest) {
		PaymentCommand paymentCommand = paymentRequest.toCommand(userId);
		PaymentInfo paymentInfo = paymentFacade.checkout(paymentCommand);
		PaymentV1Dto.PaymentResponse response = PaymentV1Dto.PaymentResponse.from(paymentInfo);
		return ApiResponse.success(response);
	}

	@PostMapping("/callback")
	@Override
	public ApiResponse<PaymentV1Dto.PaymentResponse> requestPaymentCallback(@RequestBody PaymentCallbackRequest paymentCallbackRequest) {
		PaymentInfo paymentInfo = paymentFacade.callback(paymentCallbackRequest);
		PaymentV1Dto.PaymentResponse response = PaymentV1Dto.PaymentResponse.from(paymentInfo);
		return ApiResponse.success(response);
	}
}
