package com.loopers.interfaces.api.product;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product V1 API", description = "상품 API 입니다.")
public interface ProductV1ApiSpec {

	@Operation(summary = "상품 목록 조회")
	ApiResponse<ProductV1Dto.ProductListResponse> getProductList(
			ProductV1Dto.ProductListRequest productListRequest
	);

	@Operation(summary = "상품 상세 조회")
	ApiResponse<ProductV1Dto.ProductResponse> getProductDetail(
			Long productId
	);

}
