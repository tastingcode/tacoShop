package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointInfo;
import com.loopers.application.point.PointService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.PointV1Dto.PointResponse;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/points")
public class PointV1Controller implements PointV1ApiSpec{
	private final PointService pointService;

    @PostMapping("/charge")
    @Override
    public ApiResponse<PointV1Dto.PointResponse> chargePoint(
        @RequestHeader("X-USER-ID") String userId, 
        @RequestBody PointV1Dto.PointRequest request
    ) {
		PointInfo pointInfo = pointService.chargePoint(userId, request.amount());
		PointResponse response = PointResponse.from(pointInfo);
        return ApiResponse.success(response);
    }

    @GetMapping
    @Override
    public ApiResponse<PointV1Dto.PointResponse> getUserPoint(
            @RequestHeader(value = "X-USER-ID", required = false) String userId) {
        if (userId == null || userId.isBlank()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "X-USER-ID 헤더가 필요합니다.");
        }

		PointInfo pointInfo = pointService.getUserPoint(userId);
		PointResponse response = PointResponse.from(pointInfo);
        return ApiResponse.success(response);
    }

}
