package com.loopers.interfaces.api.product;
import com.loopers.application.product.ProductInfo;
import com.loopers.application.product.ProductListInfo;
import com.loopers.application.product.ProductQuery;
import com.loopers.application.product.ProductService;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductV1ApiController implements ProductV1ApiSpec {
	private final ProductService productService;

	@GetMapping
	@Override
	public ApiResponse<ProductV1Dto.ProductListResponse> getProductList(ProductV1Dto.ProductListRequest productListRequest) {
		ProductQuery productQuery = ProductQuery.of(productListRequest.brandId(),
				productListRequest.productSortType(),
				productListRequest.page(),
				productListRequest.size());

		ProductListInfo productListInfo = productService.getProductList(productQuery);
		ProductV1Dto.ProductListResponse response = ProductV1Dto.ProductListResponse.from(productListInfo);
		return ApiResponse.success(response);
	}

	@GetMapping("/{productId}")
	@Override
	public ApiResponse<ProductV1Dto.ProductResponse> getProductDetail(@PathVariable(value = "productId") Long productId) {
		ProductInfo productDetail = productService.getProductDetail(productId);
		ProductV1Dto.ProductResponse response = ProductV1Dto.ProductResponse.from(productDetail);
		return ApiResponse.success(response);
	}
}
