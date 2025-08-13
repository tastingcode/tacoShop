package com.loopers.application.product;

import com.loopers.domain.product.constant.ProductSortType;
import lombok.Getter;

@Getter
public class ProductQuery {
	private Long brandId;
	private ProductSortType sortType;
	private int page;
	private int size;

	public static ProductQuery of(Long brandId, ProductSortType sortType, int page, int size) {
		ProductQuery productQuery = new ProductQuery();
		productQuery.brandId = brandId;
		productQuery.sortType = sortType == null ? ProductSortType.LATEST : sortType;
		productQuery.page = page;
		productQuery.size = size;
		return productQuery;
	}

}
