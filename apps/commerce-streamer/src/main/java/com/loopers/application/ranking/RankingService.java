package com.loopers.application.ranking;

import com.loopers.domain.metrics.MetricType;
import com.loopers.domain.ranking.RankingDomainService;
import com.loopers.domain.ranking.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RankingService {

	private final RankingRepository rankingRepository;
	private final RankingDomainService rankingDomainService;

	private static final Map<MetricType, Double> SCORE_MAP = Map.of(
			MetricType.PRODUCT_SALES, 1.0,
			MetricType.PRODUCT_LIKE, 0.1,
			MetricType.PRODUCT_VIEWED, 0.052
	);

	public void updateRanking(Long productId, MetricType metricType) {
		// 일간 랭킹 키
		String dailyKey = rankingDomainService.getDailyKey();
		Double score = SCORE_MAP.get(metricType);

		rankingRepository.updateScore(dailyKey, productId, score);

		rankingRepository.setCacheExpiration(dailyKey, Duration.ofDays(2));
	}
}
