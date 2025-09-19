package com.loopers.domain.ranking;


import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RankingDomainService {

	public Map<Long, Double> getScoreMap(List<? extends RankedProduct> items) {
		return items.stream()
				.collect(Collectors.groupingBy(
						RankedProduct::productId,
						Collectors.summingDouble(RankedProduct::score)
				));
	}

	public List<RankedProduct> getRankingProducts(Map<Long, Double> scoreMap) {
		return scoreMap.entrySet().stream()
				.map(e -> new RankedProduct(e.getKey(), e.getValue()))
				.sorted(Comparator.comparingDouble(RankedProduct::score).reversed())
				.limit(100)
				.toList();
	}

	public double calcDailyScore(WeeklyRanking weekly){
		long days = ChronoUnit.DAYS.between(weekly.getWeekStart(), weekly.getWeekEnd()) + 1;
		return weekly.getScore() / days;
	}

	public long calcOverlapDays(WeeklyRanking weekly, LocalDate monthStart, LocalDate monthEnd){
		LocalDate start = weekly.getWeekStart().isBefore(monthStart) ? monthStart : weekly.getWeekStart();
		LocalDate end = weekly.getWeekEnd().isAfter(monthEnd) ? monthEnd : weekly.getWeekEnd();
		return ChronoUnit.DAYS.between(start, end) + 1;
	}

}
