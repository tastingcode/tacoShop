package com.loopers.domain.product;

import com.loopers.application.product.ProductQuery;
import com.loopers.domain.brand.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProductDomainService {
    private final ProductRepository productRepository;

    public Optional<Product> getProduct(Long productId) {
        return productRepository.findById(productId);
    }

    public Page<ProductDetail> getProducts(ProductQuery productQuery) {
        return productRepository.findProductsWithBrand(productQuery);
    }
    
    public ProductDetail assembleProductDetail(Product product, Brand brand) {
        return ProductDetail.of(product, brand);
    }
}
