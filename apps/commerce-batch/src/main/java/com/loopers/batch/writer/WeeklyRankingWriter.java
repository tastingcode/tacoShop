package com.loopers.batch.writer;

import com.loopers.domain.ranking.RankedProduct;
import com.loopers.domain.ranking.RankingDomainService;
import com.loopers.domain.ranking.WeeklyRanking;
import com.loopers.domain.ranking.WeeklyRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@StepScope
public class WeeklyRankingWriter implements ItemWriter<RankedProduct> {

    private final WeeklyRankingRepository weeklyRankingRepository;

	private final RankingDomainService rankingDomainService;

    @Value("#{jobParameters['startDate']}")
    private String startDate;

    @Value("#{jobParameters['endDate']}")
    private String endDate;

    @Override
    public void write(Chunk<? extends RankedProduct> items) {
        if (items.isEmpty()) return;

        LocalDate parsedStartDate = LocalDate.parse(startDate);
        LocalDate parsedEndDate = LocalDate.parse(endDate);

		Map<Long, Double> scoreMap = rankingDomainService.getScoreMap(items.getItems());

		List<RankedProduct> rankingProducts = rankingDomainService.getRankingProducts(scoreMap);

		List<WeeklyRanking> WeeklyRankingList = new ArrayList<>();
		int i = 1;
		for (RankedProduct rankingProduct : rankingProducts) {
			WeeklyRanking weeklyRanking = WeeklyRanking.create(i++, rankingProduct.productId(), rankingProduct.score(), parsedStartDate, parsedEndDate);
			WeeklyRankingList.add(weeklyRanking);
		}

        weeklyRankingRepository.saveAll(WeeklyRankingList);
    }

}
