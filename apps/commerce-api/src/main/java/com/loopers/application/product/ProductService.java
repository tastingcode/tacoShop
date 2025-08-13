package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandDomainService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDetail;
import com.loopers.domain.product.ProductDomainService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductDomainService productDomainService;
    private final BrandDomainService brandDomainService;

	@Transactional(readOnly = true)
	public ProductListInfo getProductList(ProductQuery productQuery) {
		// 상품 목록 조회
        Page<ProductDetail> productDetailPage = productDomainService.getProducts(productQuery);
		List<ProductInfo> productInfoList = productDetailPage.getContent().stream()
				.map(ProductInfo::from)
				.toList();
		return ProductListInfo.from(productInfoList, productDetailPage.getPageable(), productDetailPage.getTotalElements());
    }

	@Transactional(readOnly = true)
	public ProductInfo getProductDetail(Long productId) {
        Product product = productDomainService.getProduct(productId).orElseThrow(
                () -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));
        Brand brand = brandDomainService.getBrand(product.getBrandId()).orElse(null);

        ProductDetail productDetail = productDomainService.assembleProductDetail(product, brand);
        return ProductInfo.from(productDetail);
    }
}
