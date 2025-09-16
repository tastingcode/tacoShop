package com.loopers.application.ranking;

import com.loopers.application.metrics.ProductMetricsCommand;
import com.loopers.domain.metrics.MetricType;
import com.loopers.domain.metrics.ProductMetrics;
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
			MetricType.PRODUCT_UNLIKE, -0.1,
			MetricType.PRODUCT_VIEWED, 0.05
	);

	private final Double LIKE_WEIGHT = 0.1;
	private final Double SALES_WEIGHT = 1.0;
	private final Double VIEWED_WEIGHT = 0.05;

	public void updateRanking(Long productId, MetricType metricType) {
		// 일간 랭킹 키
		String dailyKey = rankingDomainService.getDailyKey();
		Double score = SCORE_MAP.get(metricType);

		rankingRepository.updateScore(dailyKey, productId, score);

		rankingRepository.setCacheExpiration(dailyKey, Duration.ofDays(2));
	}

	public void updateRankingByMetrics(ProductMetrics productMetrics) {
		String dailyKey = rankingDomainService.getDailyKey();
		double score = productMetrics.getLikesDelta() * LIKE_WEIGHT + productMetrics.getSalesDelta() * SALES_WEIGHT + productMetrics.getViewsDelta() * VIEWED_WEIGHT;
		rankingRepository.updateScore(dailyKey, productMetrics.getProductId(), score);
		rankingRepository.setCacheExpiration(dailyKey, Duration.ofDays(2));
	}
}
