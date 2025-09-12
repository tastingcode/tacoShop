package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderCommand;
import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderInfo;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderV1ApiController implements OrderV1ApiSpec {

	private final OrderFacade orderFacade;

	@PostMapping
	@Override
	public ApiResponse<OrderV1Dto.OrderResponse> createOrder(@RequestHeader("X-USER-ID") String userId,
											   @RequestBody OrderV1Dto.OrderRequest orderRequest) {
		OrderCommand command = orderRequest.toCommand(userId);
		OrderInfo orderInfo = orderFacade.createOrder(command);
		OrderV1Dto.OrderResponse response = OrderV1Dto.OrderResponse.from(orderInfo);

		return ApiResponse.success(response);
	}

}
