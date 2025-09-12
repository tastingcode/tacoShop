package com.loopers.application.ranking;

import com.loopers.domain.product.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RankingAssembler {

	public List<RankingInfo> getRankingProducts(List<Long> productIds,
												Map<Long, Product> productsMap,
												long start) {

		long currentRank = start + 1;
		List<RankingInfo> rankingInfoList = new ArrayList<>();

		for (Long productId : productIds) {
			Product product = productsMap.get(productId);
			RankingInfo rankingInfo = RankingInfo.of(product, currentRank);
			rankingInfoList.add(rankingInfo);
			currentRank++;
		}

		return rankingInfoList;
	}

}
