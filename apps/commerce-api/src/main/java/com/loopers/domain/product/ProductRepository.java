package com.loopers.domain.product;

import com.loopers.application.product.ProductQuery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
	Product save(Product product);

	Optional<Product> findById(Long id);

	Optional<Product> findByIdForUpdate(Long id);

	Page<ProductDetail> findProductsWithBrand(ProductQuery productQuery);

	List<Product> findAllById(List<Long> productIds);

	List<Product> saveAll(List<Product> products);

	List<Product> findAllByIdIn(List<Long> productIds);

}
