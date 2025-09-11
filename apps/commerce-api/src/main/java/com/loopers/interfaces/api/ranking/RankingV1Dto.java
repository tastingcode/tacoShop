package com.loopers.interfaces.api.ranking;

import com.loopers.application.ranking.RankingInfo;

import java.util.List;

public class RankingV1Dto {

    public record RankingResponse(
            Long productId,
            String productName,
            int price,
			int stock,
			Long brandId,
            int likeCount,
            long rank
    ) {

        public static List<RankingResponse> from(List<RankingInfo> infos) {
            return infos.stream()
                    .map(info -> new RankingResponse(
                            info.productId(),
                            info.productName(),
                            info.price(),
							info.stock(),
							info.brandId(),
                            info.likeCount(),
                            info.rank()
                    ))
                    .toList();
        }

    }

}
