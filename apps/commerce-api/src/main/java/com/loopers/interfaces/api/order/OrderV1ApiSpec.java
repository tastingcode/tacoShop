package com.loopers.interfaces.api.order;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Order V1 API", description = "주문 API 입니다.")
public interface OrderV1ApiSpec {

	@PostMapping("/api/v1/orders")
	@Operation(summary = "주문 API")
	ApiResponse<OrderV1Dto.OrderResponse> createOrder(@RequestHeader("X-USER-ID") String userId, @RequestBody OrderV1Dto.OrderRequest orderRequest);
}
