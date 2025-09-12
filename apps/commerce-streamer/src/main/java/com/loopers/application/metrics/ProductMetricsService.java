package com.loopers.application.metrics;

import com.loopers.application.ranking.RankingService;
import com.loopers.domain.metrics.MetricType;
import com.loopers.domain.metrics.ProductMetrics;
import com.loopers.domain.metrics.ProductMetricsDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMetricsService {

	private final ProductMetricsDomainService productMetricsDomainService;
	private final RankingService rankingService;

	public void updateMetricsAndRanking(List<ProductMetricsCommand> commands, Set<String> eventIdSet) {
		// 처리되지 않은 집계 커맨드 목록 맵 추출
		Map<Long, List<ProductMetricsCommand>> commandMap = extractUnhandledCommandMap(commands, eventIdSet);

		for (Map.Entry<Long, List<ProductMetricsCommand>> entry : commandMap.entrySet()) {
			// ProductMetrics 조회
			Long productId = entry.getKey();
			ProductMetrics productMetrics = productMetricsDomainService.getProductMetrics(productId);


			// ProductMetrics 업데이트
			List<ProductMetricsCommand> productMetricsCommands = entry.getValue();
			for (ProductMetricsCommand command : productMetricsCommands) {
				productMetricsDomainService.updateMetricsDeltas(productMetrics, MetricType.from(command.eventType()), command.delta());
			}

			// ProductMetrics 저장
			ProductMetrics saveMetrics = productMetricsDomainService.saveMetrics(productMetrics);

			// 랭킹 처리
			rankingService.updateRankingByMetrics(saveMetrics);
		}
	}

	public Map<Long, List<ProductMetricsCommand>> extractUnhandledCommandMap(List<ProductMetricsCommand> commands, Set<String> eventIdSet) {
		Map<Long, List<ProductMetricsCommand>> metricsCommandMap = commands.stream()
				.filter(command -> !eventIdSet.contains(command.eventId()))
				.collect(Collectors.groupingBy(ProductMetricsCommand::ProductId));

		return metricsCommandMap;
	}

}
