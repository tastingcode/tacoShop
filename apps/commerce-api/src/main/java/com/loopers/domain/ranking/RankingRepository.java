package com.loopers.domain.ranking;

import java.util.List;

public interface RankingRepository {

	List<Long> getRankingProductIds(String rankingKey, long start, long end);

	Long getRank(String rankingKey, Long productId);
}
