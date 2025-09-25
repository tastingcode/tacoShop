package com.loopers.batch.writer;

import com.loopers.domain.ranking.MonthlyRanking;
import com.loopers.domain.ranking.MonthlyRankingRepository;
import com.loopers.domain.ranking.RankedProduct;
import com.loopers.domain.ranking.RankingDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@StepScope
public class MonthlyRankingWriter implements ItemWriter<RankedProduct> {

    private final MonthlyRankingRepository monthlyRankingRepository;

	private final RankingDomainService rankingDomainService;

    @Value("#{jobParameters['yearMonth']}")
    private String yearMonthStr;

    @Override
    public void write(Chunk<? extends RankedProduct> items) {
        if (items.isEmpty()) return;

        YearMonth yearMonth = YearMonth.parse(yearMonthStr);

		Map<Long, Double> scoreMap = rankingDomainService.getScoreMap(items.getItems());

		List<RankedProduct> rankingProducts = rankingDomainService.getRankingProducts(scoreMap);

		List<MonthlyRanking> MonthlyRankingList = new ArrayList<>();
		int i = 1;
		for (RankedProduct rankingProduct : rankingProducts) {
			MonthlyRanking monthlyRanking = MonthlyRanking.create(i++, rankingProduct.productId(), rankingProduct.score(), yearMonth);
			MonthlyRankingList.add(monthlyRanking);
		}
		monthlyRankingRepository.saveAll(MonthlyRankingList);
    }

}
