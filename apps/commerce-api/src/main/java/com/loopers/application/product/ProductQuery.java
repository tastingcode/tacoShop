package com.loopers.application.product;

import com.loopers.domain.product.constant.ProductSortType;
import lombok.Getter;

@Getter
public class ProductQuery {
	private Long brandId;
	private ProductSortType sortType;
	private int page;
	private int size;

	public static ProductQuery of(Long brandId, ProductSortType sortType, Integer page, Integer size) {
		ProductQuery productQuery = new ProductQuery();
		productQuery.brandId = brandId;
		productQuery.sortType = sortType == null ? ProductSortType.LATEST : sortType;
		productQuery.page = (page == null) ? 0 : page ;
		productQuery.size = (size == null) ? 10 : size ;
		return productQuery;
	}

}
