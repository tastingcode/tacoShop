package com.loopers.interfaces.api.ranking;

import com.loopers.application.ranking.RankingInfo;
import com.loopers.application.ranking.RankingListInfo;

import java.util.List;

public class RankingV1Dto {

    public record RankingListResponse(
			List<RankingInfo> rankingInfos,
			int page,
			int size,
			long totalCount,
			int totalPage
    ) {
		public static RankingListResponse from(RankingListInfo rankingListInfo) {
			return new RankingListResponse(rankingListInfo.rankingInfos(),
					rankingListInfo.page(),
					rankingListInfo.size(),
					rankingListInfo.totalCount(),
					rankingListInfo.totalPage());
		}
    }
}
