package com.loopers.batch.processor;

import com.loopers.domain.ranking.RankedProduct;
import com.loopers.domain.ranking.RankingDomainService;
import com.loopers.domain.ranking.WeeklyRanking;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;

@Component
@RequiredArgsConstructor
@StepScope
public class MonthlyRankingProcessor implements ItemProcessor<WeeklyRanking, RankedProduct> {

	private final RankingDomainService rankingDomainService;

    @Value("#{jobParameters['yearMonth']}")
    private String yearMonthStr;

    @Override
    public RankedProduct process(WeeklyRanking weekly) {
        YearMonth yearMonth = YearMonth.parse(yearMonthStr);
        LocalDate monthStart = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();

		double dailyScore = rankingDomainService.calcDailyScore(weekly);

		long overlapDays = rankingDomainService.calcOverlapDays(weekly, monthStart, monthEnd);

        double monthScore = dailyScore * overlapDays;

        return new RankedProduct(weekly.getProductId(), monthScore);

    }

}
