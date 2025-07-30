package com.loopers.domain.product;

import com.loopers.application.product.ProductQuery;
import org.springframework.data.domain.Page;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    Page<ProductDetail> findProductsWithBrand(ProductQuery productQuery);
}
