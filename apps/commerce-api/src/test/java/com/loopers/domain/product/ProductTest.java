package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ProductTest {
	/**
	 * 상품 단위 테스트
	 * - [x] 상품 재고 차감 시 주문 수량이 0개 이하인 경우 에러가 발생한다.
	 * - [x] 상품 재고 차감 시 주문 수량 만큼 재고가 차감된다.
	 */
	@DisplayName("재고 차감 테스트")
	@Nested
	class Deduct {
		@Test
		@DisplayName("상품 재고 차감 시 주문 수량이 0개 이하인 경우 에러가 발생한다.")
		void returnsBadRequest_whenQuantityIsLessThanZero() {
			// arrange
			Product product = Product.builder()
					.name("상품")
					.price(10000)
					.stock(10)
					.brandId(1L)
					.likeCount(0)
					.build();

			// act
			CoreException exception = assertThrows(CoreException.class, () -> product.deductStock(-1));

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
		}

		@DisplayName("상품 재고 차감 시 주문 수량 만큼 재고가 차감된다.")
		@Test
		void deductStock_whenStockIsEnough() {
		    // arrange
			Product product = Product.builder()
					.name("상품")
					.price(10000)
					.stock(10)
					.brandId(1L)
					.likeCount(0)
					.build();
			int currentStock = product.getStock();

		    // act
			int quantity = 3;
			product.deductStock(quantity);

		    // assert
			assertThat(product.getStock()).isEqualTo(currentStock- quantity);
		}
	}


}
