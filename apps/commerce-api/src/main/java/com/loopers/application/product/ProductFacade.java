package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDetail;
import com.loopers.domain.product.ProductService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductFacade {
    private final ProductService productService;
    private final BrandService brandService;

    public ProductListInfo getProductList(ProductQuery productQuery) {
		// 상품 목록 조회
        Page<ProductDetail> productDetailPage = productService.getProducts(productQuery);
		List<ProductInfo> productInfoList = productDetailPage.getContent().stream()
				.map(ProductInfo::from)
				.toList();
		return ProductListInfo.from(productInfoList, productDetailPage.getPageable(), productDetailPage.getTotalElements());
    }

    public ProductInfo getProductDetail(Long productId) {
        Product product = productService.getProduct(productId).orElseThrow(
                () -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));
        Brand brand = brandService.getBrand(product.getBrandId()).orElse(null);

        ProductDetail productDetail = productService.assembleProductDetail(product, brand);
        return ProductInfo.from(productDetail);
    }
}
