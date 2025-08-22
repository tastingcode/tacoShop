package com.loopers.infrastructure.payment.pg;


import com.loopers.support.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
		name = "LuckyPgClient",
		url = "http://localhost:8082",
		configuration = FeignClientConfig.class
)
public interface LuckyPgClient {

	@PostMapping("/api/v1/payments")
	PgDto.PgResponse requestPayment(@RequestHeader("X-USER-ID") Long merchantId, @RequestBody PgDto.PgRequest request);

	@GetMapping("/api/v1/payments/{transactionKey}")
	PgDto.PgResponse requestPaymentInfo(@RequestHeader("X-USER-ID") Long merchantId, @PathVariable("transactionKey") String transactionKey);

}
