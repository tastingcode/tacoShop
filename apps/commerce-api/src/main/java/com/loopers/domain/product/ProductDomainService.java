package com.loopers.domain.product;

import com.loopers.application.product.ProductQuery;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.order.OrderProduct;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductDomainService {
	private final ProductRepository productRepository;

	public Product getProduct(Long productId) {
		return productRepository.findById(productId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));
	}

	public Page<ProductDetail> getProductsByQuery(ProductQuery productQuery) {
		return productRepository.findProductsWithBrand(productQuery);
	}
	public List<Product> getProductsByOrderProducts(List<OrderProduct> orderProducts) {
		List<Long> productIds = orderProducts.stream()
				.map(OrderProduct::getProductId)
				.toList();
		return productRepository.findAllById(productIds);
	}

	public ProductDetail assembleProductDetail(Product product, Brand brand) {
		return ProductDetail.of(product, brand);
	}

}
