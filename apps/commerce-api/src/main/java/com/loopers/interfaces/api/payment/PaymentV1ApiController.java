package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentCommand;
import com.loopers.application.payment.PaymentFacade;
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
	public ApiResponse<Object> requestPayment(@RequestHeader("X-USER-ID") String userId, @RequestBody PaymentV1Dto.PaymentRequest paymentRequest) {
		PaymentCommand paymentCommand = PaymentCommand.from(userId,
				paymentRequest.orderId(),
				paymentRequest.paymentType(),
				paymentRequest.cardType(),
				paymentRequest.cardNo(),
				paymentRequest.amount());

		paymentFacade.checkout(paymentCommand);
		return ApiResponse.success();
	}

	@PostMapping("/callback")
	@Override
	public ApiResponse<Object> requestPaymentCallback(@RequestBody PaymentCallbackRequest paymentCallbackRequest) {
		paymentFacade.callback(paymentCallbackRequest);
		return ApiResponse.success();
	}


}
