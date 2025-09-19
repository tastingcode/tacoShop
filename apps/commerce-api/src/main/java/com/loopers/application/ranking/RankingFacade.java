package com.loopers.application.ranking;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDomainService;
import com.loopers.domain.ranking.RankingDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RankingFacade {

	private final ProductDomainService productDomainService;
	private final RankingAssembler rankingAssembler;
	private final RankingService rankingService;

	public RankingListInfo getRankingList(RankingCommand command) {
		// 랭킹 목록(상품 아이디) 조회
		RankingResult rankingResult = switch (command.period()) {
			case DAILY -> rankingService.getDailyRankings(command);
			case WEEKLY -> rankingService.getWeeklyRankings(command);
			case MONTHLY -> rankingService.getMonthlyRankings(command);
		};

		// 상품 목록 Map 조회
		Map<Long, Product> productsMap = productDomainService.getProductsMap(rankingResult.productIds());

		// 랭킹 상품 조회
		List<RankingInfo> rankingInfos = rankingAssembler.getRankingProducts(rankingResult.productIds(), productsMap, command.start());

		// 랭킹 목록 정보
		return RankingListInfo.from(rankingInfos, command.page(), command.size(), rankingResult.totalCount());
	}
}
