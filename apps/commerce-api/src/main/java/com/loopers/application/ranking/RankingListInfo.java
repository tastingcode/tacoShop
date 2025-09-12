package com.loopers.application.ranking;

import java.util.List;

public record RankingListInfo(
		List<RankingInfo> rankingInfos,
		int page,
		int size,
		long totalCount,
		int totalPage
) {
	public static RankingListInfo from(List<RankingInfo> rankingInfos,
									 int page,
									 int size,
									 long totalCount) {
		return new RankingListInfo(rankingInfos,
				page,
				size,
				totalCount,
				(int) Math.ceil((double) totalCount / size));
	}
}
