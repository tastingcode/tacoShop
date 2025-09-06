package com.loopers.domain.product;

import com.loopers.application.product.ProductQuery;

import java.util.Locale;

public class ProductCacheKeyGenerator {
	public static String getKey(ProductQuery productQuery) {
		StringBuilder keyBuilder = new StringBuilder("product-list");

		if (productQuery.getBrandId() != null) {
			keyBuilder.append("-brandId:").append(productQuery.getBrandId());
		}

		if (productQuery.getSortType() != null) {
			keyBuilder.append("-sortType:").append(productQuery.getSortType().name().toLowerCase());
		}

		keyBuilder.append("-page:").append(productQuery.getPage());
		keyBuilder.append("-size:").append(productQuery.getSize());

		return keyBuilder.toString();

	}
}
