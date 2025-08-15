package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductInfo;
import com.loopers.application.product.ProductListInfo;
import com.loopers.domain.product.constant.ProductSortType;

import java.util.List;

public class ProductV1Dto {

	public record ProductListRequest(
			Long brandId,
			ProductSortType productSortType,
			Integer page,
			Integer size
	){}

	public record ProductResponse(
			Long brandId,
			String brandName,
			Long productId,
			String productName,
			int price,
			int stock,
			int likeCount
	){
		public static ProductResponse from(ProductInfo productInfo) {
			return new ProductResponse(productInfo.brandId(),
					productInfo.brandName(),
					productInfo.productId(),
					productInfo.productName(),
					productInfo.price(),
					productInfo.stock(),
					productInfo.likeCount());
		}
	}

	public record ProductListResponse(
			long totalSize,
			int page,
			int size,
			List<ProductResponse> productResponseList
	){
		public static ProductListResponse from(ProductListInfo productListInfo) {
			List<ProductResponse> productResponses = productListInfo.productInfoList().stream()
					.map(ProductResponse::from)
					.toList();

			return new ProductListResponse(productListInfo.totalSize(),
					productListInfo.page(),
					productListInfo.size(),
					productResponses);

		}
	}
}
