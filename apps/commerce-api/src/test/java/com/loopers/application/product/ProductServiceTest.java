package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.constant.ProductSortType;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@Autowired
	private BrandJpaRepository brandJpaRepository;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	/**
	 * 상품 목록 조회 통합 테스트
	 * - [x] 상품 정보 객체는 브랜드 정보, 좋아요 수를 포함한다.
	 * - [x] 상품 목록 조회 시 최신순 기준으로 조회할 수 있다.
	 * - [x] 상품 목록 조회 시 가격 오름차순 기준으로 조회할 수 있다.
	 * - [x] 상품 목록 조회 시 좋아요 내림차순 기준으로 조회할 수 있다.
	 */
	@Nested
	@DisplayName("상품 목록 조회")
	class ProductList {
		private Brand savedBrand1;
		private Brand savedBrand2;
		private Product savedProduct1;
		private Product savedProduct2;
		private Product savedProduct3;

		@BeforeEach
		void initProduct() {
			// 브랜드 데이터 생성
			Brand brand1 = Brand.builder()
					.name("브랜드A")
					.description("브랜드A 설명")
					.build();

			Brand brand2 = Brand.builder()
					.name("브랜드B")
					.description("브랜드B 설명")
					.build();

			savedBrand1 = brandJpaRepository.save(brand1);
			savedBrand2 = brandJpaRepository.save(brand2);

			// 상품 데이터 생성 (정렬 테스트를 위해 다양한 값 설정)
			Product product1 = Product.builder()
					.name("상품1")
					.price(10000)  // 가격: 중간
					.stock(10)
					.brandId(savedBrand1.getId())
					.likeCount(100)  // 좋아요: 중간
					.build();

			Product product2 = Product.builder()
					.name("상품2")
					.price(5000)   // 가격: 낮음
					.stock(20)
					.brandId(savedBrand2.getId())
					.likeCount(10) // 좋아요: 높음
					.build();

			Product product3 = Product.builder()
					.name("상품3")
					.price(20000)  // 가격: 높음
					.stock(5)
					.brandId(savedBrand1.getId())
					.likeCount(1)  // 좋아요: 낮음
					.build();

			savedProduct1 = productJpaRepository.save(product1);
			savedProduct2 = productJpaRepository.save(product2);
			savedProduct3 = productJpaRepository.save(product3);
		}

		@DisplayName("상품 정보 객체는 브랜드 정보, 좋아요 수를 포함한다.")
		@Test
		void isProductInfoWithBrandAndLikeCount() {
		    // arrange
			ProductQuery query = ProductQuery.of(null, ProductSortType.LATEST, 0, 10);

		    // act
			ProductListInfo productListInfo = productService.getProductList(query);
			List<ProductInfo> productInfoList = productListInfo.productInfoList();

		    // assert
			assertAll(
					() -> assertThat(productInfoList).isNotNull(),
					() -> assertThat(productListInfo.totalSize()).isEqualTo(3),
					() -> assertThat(productInfoList.get(0).brandName()).isEqualTo(savedBrand1.getName()),
					() -> assertThat(productInfoList.get(0).likeCount()).isEqualTo(savedProduct3.getLikeCount())
			);
		}

		@DisplayName("상품 목록 조회 최신순")
		@Test
		void getProductListOrderByLatest() {
			// arrange
			ProductQuery query = ProductQuery.of(null, ProductSortType.LATEST, 0, 10);

			// act
			ProductListInfo productListInfo = productService.getProductList(query);
			List<ProductInfo> productInfoList = productListInfo.productInfoList();

			// assert
			assertAll(
					() -> assertThat(productInfoList).isNotNull(),
					() -> assertThat(productListInfo.totalSize()).isEqualTo(3),
					() -> assertThat(productInfoList.get(0).productId()).isEqualTo(savedProduct3.getId())
			);
		}

		@DisplayName("상품 목록 조회 가격 오름차순")
		@Test
		void getProductListOrderByPriceAsc() {
			// arrange
			ProductQuery query = ProductQuery.of(null, ProductSortType.PRICE_ASC, 0, 10);

			// act
			ProductListInfo productListInfo = productService.getProductList(query);
			List<ProductInfo> productInfoList = productListInfo.productInfoList();

			// assert
			assertAll(
					() -> assertThat(productInfoList).isNotNull(),
					() -> assertThat(productListInfo.totalSize()).isEqualTo(3),
					() -> assertThat(productInfoList.get(0).productId()).isEqualTo(savedProduct2.getId())
			);
		}



		@DisplayName("상품 목록 조회 좋아요 내림차순")
		@Test
		void getProductListOrderByLikesDesc() {
		    // arrange
			ProductQuery query = ProductQuery.of(null, ProductSortType.LIKES_DESC, 0, 10);

			// act
			ProductListInfo productListInfo = productService.getProductList(query);
			List<ProductInfo> productInfoList = productListInfo.productInfoList();

			// assert
			assertAll(
					() -> assertThat(productInfoList).isNotNull(),
					() -> assertThat(productListInfo.totalSize()).isEqualTo(3),
					() -> assertThat(productInfoList.get(0).productId()).isEqualTo(savedProduct1.getId())
			);
		}

	}

	/**
	 * 상품 목록 조회 통합 테스트
	 * - [x] 상품 정보 객체는 브랜드 정보, 좋아요 수를 포함한다.
	 */
	@Nested
	@DisplayName("상품 상세 조회")
	class ProductDetail {
		@DisplayName("상품 상세 조회 시 브랜드 정보, 좋아요 수를 포함한다.")
		@Test
		void isExistBrandInfoAndLikeCountInProductDetail() {
		    // arrange
			String initProductName = "상품";
			int initLikeCount = 5;
			Product product = Product.builder()
					.name(initProductName)
					.price(10000)
					.stock(10)
					.brandId(1L)
					.likeCount(initLikeCount)
					.build();

			String initBrandName = "브랜드";
			Brand brand = Brand.builder()
					.name(initBrandName)
					.description("브랜드 설명")
					.build();

			Product saveProduct = productJpaRepository.save(product);
			Brand saveBrand = brandJpaRepository.save(brand);

		    // act
			ProductInfo productInfo = productService.getProductDetail(saveProduct.getId());

			// assert
			assertAll(
					() -> assertThat(productInfo.productId()).isEqualTo(saveProduct.getId()),
					() -> assertThat(productInfo.brandId()).isEqualTo(saveBrand.getId()),
					() -> assertThat(productInfo.productName()).isEqualTo(initProductName),
					() -> assertThat(productInfo.likeCount()).isEqualTo(initLikeCount),
					() -> assertThat(productInfo.brandName()).isEqualTo(initBrandName)
			);
		}
	}

}
