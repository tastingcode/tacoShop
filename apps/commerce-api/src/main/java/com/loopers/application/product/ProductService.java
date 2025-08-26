package com.loopers.application.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandDomainService;
import com.loopers.domain.product.*;
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
	private final ProductCacheRepository productCacheRepository;
	private final ObjectMapper objectMapper;

	@Transactional(readOnly = true)
	public ProductListInfo getProductList(ProductQuery productQuery) {
		// 상품 목록 캐시 조회
		boolean cacheable = (productQuery.getPage() == 0 || productQuery.getPage() == 1);
		String cachedData = null;
		if (cacheable) {
			String cacheKey = ProductCacheKeyGenerator.getKey(productQuery);
			cachedData = productCacheRepository.get(cacheKey);
		}

		if (cachedData != null){
			try {
				ProductListInfo productListInfo = objectMapper.readValue(cachedData, ProductListInfo.class);
				return objectMapper.readValue(cachedData, ProductListInfo.class);
			} catch (JsonProcessingException e) {
				throw new CoreException(ErrorType.INTERNAL_ERROR);
			}
		}

		// 상품 목록 조회
		Page<ProductDetail> productDetailPage = productDomainService.getProducts(productQuery);
		List<ProductInfo> productInfoList = productDetailPage.getContent().stream()
				.map(ProductInfo::from)
				.toList();
		ProductListInfo productListInfo = ProductListInfo.from(productInfoList, productDetailPage.getPageable(), productDetailPage.getTotalElements());

		// 상품 목록 캐시 저장
		if (cacheable){
			String cacheKey = ProductCacheKeyGenerator.getKey(productQuery);
			try {
				productCacheRepository.set(cacheKey, productListInfo);
			} catch (JsonProcessingException e) {
				throw new CoreException(ErrorType.INTERNAL_ERROR);
			}
		}

		return productListInfo;
	}

	@Transactional(readOnly = true)
	public ProductInfo getProductDetail(Long productId) {
		Product product = productDomainService.getProduct(productId);
		Brand brand = brandDomainService.getBrand(product.getBrandId()).orElse(null);

		ProductDetail productDetail = productDomainService.assembleProductDetail(product, brand);
		return ProductInfo.from(productDetail);
	}
}
