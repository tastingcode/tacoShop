package com.loopers.application.product;

import org.springframework.data.domain.Pageable;

import java.util.List;

public record ProductListInfo(List<ProductInfo> productInfoList,
							  int page,
							  int size,
							  long totalSize,
							  int totalPage) {
	public static ProductListInfo from(List<ProductInfo> productInfoList, Pageable pageable, long totalSize) {
		return new ProductListInfo(
				productInfoList,
				pageable.getPageNumber(),
				pageable.getPageSize(),
				totalSize,
				(int) Math.ceil((double) totalSize / pageable.getPageSize())
		);
	}

}
