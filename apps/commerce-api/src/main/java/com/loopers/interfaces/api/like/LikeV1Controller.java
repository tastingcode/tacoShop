package com.loopers.interfaces.api.like;

import com.loopers.application.like.LikeInfo;
import com.loopers.application.like.LikeService;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeV1Controller implements LikeV1ApiSpec {
    private final LikeService likeService;

    @PostMapping
    @Override
    public ApiResponse<LikeV1Dto.LikeResponse> like(@RequestHeader("X-USER-ID") String userId, @RequestBody LikeV1Dto.LikeRequest request) {
		LikeInfo likeInfo = likeService.like(userId, request.productId());
		LikeV1Dto.LikeResponse response = LikeV1Dto.LikeResponse.from(likeInfo);
		return ApiResponse.success(response);
    }

    @DeleteMapping
    @Override
    public ApiResponse<LikeV1Dto.LikeResponse> unlike(@RequestHeader("X-USER-ID") String userId, @RequestBody LikeV1Dto.LikeRequest request) {
		LikeInfo likeInfo = likeService.unlike(userId, request.productId());
		LikeV1Dto.LikeResponse response = LikeV1Dto.LikeResponse.from(likeInfo);
        return ApiResponse.success(response);
    }
}
