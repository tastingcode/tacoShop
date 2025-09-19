package com.loopers.infrastructure.ranking;

import com.loopers.domain.ranking.MonthlyRanking;
import com.loopers.domain.ranking.RankingRepository;
import com.loopers.domain.ranking.WeeklyRanking;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepository {
	private final RedisTemplate<String, String> redisTemplate;
	private final WeeklyRankingJpaRepository weeklyRankingJpaRepository;
	private final MonthlyRankingJpaRepository monthlyRankingJpaRepository;

	@Override
	public List<Long> getRankingProductIds(String rankingKey, long start, long end) {
		return redisTemplate.opsForZSet().reverseRange(rankingKey, start, end).stream()
				.map(Long::valueOf)
				.toList();
	}

	@Override
	public Long getRank(String rankingKey, Long productId) {
		Long rank = redisTemplate.opsForZSet().reverseRank(rankingKey, productId.toString());
		return rank != null ? rank + 1 : null;
	}

	@Override
	public Long findTotalCount(String rankingKey) {
		return redisTemplate.opsForZSet().zCard(rankingKey);
	}

	@Override
	public Page<WeeklyRanking> findWeeklyRankingByDate(LocalDate startDate, LocalDate endDate, Pageable pageable) {
		return weeklyRankingJpaRepository.findByWeekStartAndWeekEnd(startDate, endDate, pageable);
	}

	@Override
	public Page<MonthlyRanking> findMonthlyRankingByDate(YearMonth monthlyPeriod, Pageable pageable) {
		return monthlyRankingJpaRepository.findByMonthPeriod(monthlyPeriod, pageable);
	}

}
