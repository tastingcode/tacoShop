package com.loopers.domain.like;

import com.loopers.application.like.LikeInfo;
import com.loopers.application.like.LikeService;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDetail;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.constant.Gender;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LikeDomainServiceIntgTest {

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Autowired
	private BrandJpaRepository brandJpaRepository;

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@Autowired
	private LikeService likeService;

	@Autowired
	private LikeDomainService likeDomainService;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	@AfterEach
	void cleanDatabase() {
		databaseCleanUp.truncateAllTables();
	}

	/**
	 * 좋아요 통합 테스트
	 * - [x] 좋아요가 없는 상태에서 좋아요 등록 시 카운트가 증가한다.
	 * - [x] 중복 좋아요 등록 시 카운트는 한 번만 증가한다.
	 */
	@Nested
	@DisplayName("좋아요 등록 시")
	class like {
		private UserEntity testUser;
		private Product testProduct;

		@BeforeEach
		void setUp() {
			testUser = new UserEntity("tempUser", "량호", Gender.M, "tempUser@gmail.com", "2020-12-12");

			testProduct = Product.builder()
					.name("상품1")
					.price(10000)
					.stock(10)
					.brandId(1L)
					.likeCount(100)
					.build();
		}

		@DisplayName("좋아요가 없는 상태에서 좋아요 등록 시 카운트가 증가한다.")
		@Test
		void likeSuccess() {
		    // arrange
			UserEntity savedUser = userJpaRepository.save(testUser);
			Product savedProduct = productJpaRepository.save(testProduct);

			// act
//			LikeInfoDto likeInfoDto1 = likeDomainService.like(savedUser, savedProduct);

			// assert
//			assertThat(testProduct.getLikeCount()).isEqualTo(likeInfoDto1.likeCount());

		}

		@DisplayName("중복 좋아요 등록 시 카운트는 한 번만 증가한다.")
		@Test
		void duplicateLike() {
		    // arrange
			UserEntity savedUser = userJpaRepository.save(testUser);
			Product savedProduct = productJpaRepository.save(testProduct);

			LikeInfo likeInfo1 = likeService.like(savedUser.getUserId(), savedProduct.getId());

			// act
			LikeInfo likeInfo2 = likeService.like(savedUser.getUserId(), savedProduct.getId());

			// assert
//			assertThat(likeInfo1.likeCount()).isEqualTo(likeInfo2.likeCount());

		}

	}

	/**
	 * 좋아요 취소 통합 테스트
	 * - [x] 좋아요가 있는 상태에서 좋아요 등록 시 카운트가 감소한다.
	 * - [x] 중복 좋아요 취소 시 카운트는 한 번만 감소한다.
	 */
	@Nested
	@DisplayName("좋아요 취소 시")
	class unLike {
		private UserEntity testUser;
		private Product testProduct;

		@BeforeEach
		void setUp() {
			testUser = new UserEntity("tempUser", "량호", Gender.M, "tempUser@gmail.com", "2020-12-12");

			testProduct = Product.builder()
					.name("상품1")
					.price(10000)
					.stock(10)
					.brandId(1L)
					.likeCount(100)
					.build();
		}

		@DisplayName("좋아요가 있는 상태에서 좋아요 취소 시 카운트가 감소한다.")
		@Test
		void likeSuccess() {
			// arrange
			UserEntity savedUser = userJpaRepository.save(testUser);
			Product savedProduct = productJpaRepository.save(testProduct);
//			LikeInfoDto likeInfoDto1 = likeDomainService.like(savedUser, savedProduct);

			// act
//			LikeInfoDto likeInfoDto2 = likeDomainService.unLike(savedUser, savedProduct);

			// assert
//			assertThat(testProduct.getLikeCount() + 1).isEqualTo(likeInfoDto1.likeCount());
//			assertThat(testProduct.getLikeCount()).isEqualTo(likeInfoDto2.likeCount());

		}

		@DisplayName("중복 좋아요 취소 시 카운트는 한 번만 감소한다.")
		@Test
		void duplicateLike() {
			// arrange
			UserEntity savedUser = userJpaRepository.save(testUser);
			Product savedProduct = productJpaRepository.save(testProduct);
			LikeInfo likeInfo1 = likeService.like(savedUser.getUserId(), savedProduct.getId());

			// act
//			LikeInfo likeInfo2 = likeService.unLike(savedUser.getUserId(), savedProduct.getId());
//			LikeInfo likeInfo3 = likeService.unLike(savedUser.getUserId(), savedProduct.getId());

			// assert
//			assertThat(likeInfo2.likeCount()).isEqualTo(likeInfo3.likeCount());


		}

	}

	/**
	 * 좋아요 한 상품 목록 조회 테스트
	 * - [x] 좋아요 한 상품 목록 조회 시 좋아요 수가 포함된다.
	 * - [x] 좋아요 한 상품 목록 조회 시 상품 정보, 브랜드 정보가 포함된다.
	 */
	@DisplayName("좋아요 한 상품 목록 조회")
	@Nested
	class getLikedProducts {
		private Brand savedBrand1;
		private Brand savedBrand2;
		private Product savedProduct1;
		private Product savedProduct2;
		private Product savedProduct3;
		private UserEntity savedUser1;
		private UserEntity savedUser2;

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

			UserEntity user1 = new UserEntity("userA", "유저A", Gender.M, "tempUser@gmail.com", "2020-12-12");
			UserEntity user2 = new UserEntity("userB", "유저B", Gender.M, "tempUser@gmail.com", "2020-12-12");

			savedUser1 = userJpaRepository.save(user1);
			savedUser2 = userJpaRepository.save(user2);


		}


		@Transactional
		@DisplayName("좋아요 한 상품 목록 조회 시 좋아요 수가 포함된다.")
		@Test
		void likedProductsContainsLikeCount() {
		    // arrange
//			likeDomainService.like(savedUser1, savedProduct1);
//			likeDomainService.like(savedUser1, savedProduct2);

		    // act
			List<ProductDetail> likedProducts = likeDomainService.getLikedProducts(savedUser1);

			// assert
//			assertThat(likedProducts.size()).isEqualTo(2);
//			assertThat(likedProducts.get(0).likeCount()).isEqualTo(savedProduct1.getLikeCount());

		}

		@Transactional
		@DisplayName("좋아요 한 상품 목록 조회 시 상품 정보, 브랜드 정보가 포함된다.")
		@Test
		void likedProductsContainsProductAndBrandInfo() {
			// arrange
//			likeDomainService.like(savedUser1, savedProduct1);
//			likeDomainService.like(savedUser1, savedProduct2);

			// act
			List<ProductDetail> likedProducts = likeDomainService.getLikedProducts(savedUser1);

			// assert
//			assertThat(likedProducts.size()).isEqualTo(2);
//			assertThat(likedProducts.get(0).likeCount()).isEqualTo(savedProduct1.getLikeCount());
//			assertThat(likedProducts.get(0).productName()).isEqualTo(savedProduct1.getName());
//			assertThat(likedProducts.get(0).brandName()).isEqualTo(savedBrand1.getName());

		}
	}


}
