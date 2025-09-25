package com.loopers.application.ranking;

import java.util.List;

public record RankingResult(
		List<Long> productIds,
		long totalCount
) {
	public static RankingResult of(List<Long> productIds, long totalCount) {
		return new RankingResult(productIds, totalCount);
	}
}
