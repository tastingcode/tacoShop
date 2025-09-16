package com.loopers.application.ranking;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDomainService;
import com.loopers.domain.ranking.RankingDomainService;
import com.loopers.domain.ranking.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RankingService {

	private final RankingRepository rankingRepository;
	private final RankingDomainService rankingDomainService;
	private final ProductDomainService productDomainService;
	private final RankingAssembler rankingAssembler;


	public RankingListInfo getRankingList(RankingCommand command) {
		// 랭킹 키 조회
		String dailyKey = rankingDomainService.getDailyKey(command.date());

		// 랭킹 목록(상품 아이디) 조회
		List<Long> productIds = rankingRepository.getRankingProductIds(dailyKey, command.start(), command.end());

		// 상품 목록 Map 조회
		Map<Long, Product> productsMap = productDomainService.getProductsMap(productIds);

		// 랭킹 상품 조회
		List<RankingInfo> rankingInfos = rankingAssembler.getRankingProducts(productIds, productsMap, command.start());

		// 토탈 카운트 조회
		Long totalCount = rankingDomainService.getTotalCountBy(dailyKey);

		// 랭킹 목록 정보
		return RankingListInfo.from(rankingInfos, command.page(), command.size(), totalCount);
	}

}
