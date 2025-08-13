package com.loopers.infrastructure.product;

import com.loopers.domain.product.QProduct;
import com.loopers.domain.product.constant.ProductSortType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;

import static com.loopers.domain.product.QProduct.product;


public class ProductQueryFilter {

	public static BooleanBuilder getBrandFilter(Long brandId) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		if (brandId != null) {
			booleanBuilder.and(product.brandId.eq(brandId));
		}
		return booleanBuilder;
	}

	public static OrderSpecifier<?> getOrderSpecifier(ProductSortType sortType, QProduct product) {
		return sortType == null ? product.createdAt.desc() :
				switch (sortType) {
					case LATEST -> product.createdAt.desc();
					case PRICE_ASC -> product.price.asc();
					case LIKES_DESC -> product.likeCount.desc();
				};
	}
}
