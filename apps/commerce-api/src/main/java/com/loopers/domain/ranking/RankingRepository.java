package com.loopers.domain.ranking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface RankingRepository {

	List<Long> getRankingProductIds(String rankingKey, long start, long end);

	Long getRank(String rankingKey, Long productId);

	Long findTotalCount(String rankingKey);

	Page<WeeklyRanking> findWeeklyRankingByDate(LocalDate startDate, LocalDate endDate, Pageable pageable);

	Page<MonthlyRanking> findMonthlyRankingByDate(YearMonth monthlyPeriod, Pageable pageable);

}
