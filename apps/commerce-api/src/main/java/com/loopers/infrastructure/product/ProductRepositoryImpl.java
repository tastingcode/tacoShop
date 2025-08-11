package com.loopers.infrastructure.product;

import com.loopers.application.product.ProductQuery;
import com.loopers.domain.brand.QBrand;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDetail;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.QProduct;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Product save(Product product) {
		return productJpaRepository.save(product);
	}

	@Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id);
    }

	@Override
	public Optional<Product> findByIdForUpdate(Long id) {
		return productJpaRepository.findByIdForUpdate(id);
	}

	@Override
    public Page<ProductDetail> findProductsWithBrand(ProductQuery productQuery) {
        QProduct product = QProduct.product;
        QBrand brand = QBrand.brand;

		// 정렬 조건
		OrderSpecifier<?> orderSpecifier = ProductQueryFilter.getOrderSpecifier(productQuery.getSortType(), product);

		// 상품 목록 조회
		List<ProductDetail> products = jpaQueryFactory
				.select(Projections.constructor(ProductDetail.class,
						product.id,
						product.name,
						product.price,
						product.stock,
						product.likeCount,
						brand.id,
						brand.name,
						brand.description
				))
				.from(product)
				.leftJoin(brand).on(product.brandId.eq(brand.id))
				.orderBy(orderSpecifier)
				.offset((long) productQuery.getPage() * productQuery.getSize())
				.limit(productQuery.getSize())
				.fetch();

		// 전체 카운트 조회
		int total = jpaQueryFactory
				.selectFrom(product)
				.leftJoin(brand).on(product.brandId.eq(brand.id))
				.orderBy(orderSpecifier)
				.offset((long) productQuery.getPage() * productQuery.getSize())
				.limit(productQuery.getSize())
				.fetch().size();

		Pageable pageable = PageRequest.of(productQuery.getPage(), productQuery.getSize());
        return new PageImpl<>(products, pageable, total);
    }

	@Override
	public List<Product> findAllById(List<Long> productIds) {
		return productJpaRepository.findAllById(productIds);
	}

	@Override
	public List<Product> saveAll(List<Product> products) {
		return productJpaRepository.saveAll(products);
	}

}
