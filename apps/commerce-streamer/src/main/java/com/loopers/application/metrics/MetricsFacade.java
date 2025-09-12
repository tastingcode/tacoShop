package com.loopers.application.metrics;

import com.loopers.application.auditlog.AuditLogCommand;
import com.loopers.application.ranking.RankingService;
import com.loopers.domain.eventHandled.EventHandled;
import com.loopers.domain.eventHandled.EventHandledDomainService;
import com.loopers.domain.metrics.MetricType;
import com.loopers.domain.metrics.ProductMetrics;
import com.loopers.domain.metrics.ProductMetricsDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MetricsFacade {

	private final ProductMetricsDomainService productMetricsDomainService;
	private final EventHandledDomainService eventHandledDomainService;
	private final RankingService rankingService;

	@Transactional
	public void handleMetrics(List<ProductMetricsCommand> commands) {
		// eventIds
		Set<String> eventIdSet = commands.stream()
				.map(ProductMetricsCommand::eventId)
				.collect(Collectors.toSet());

		// 이벤트 핸들 ID 조회
		Set<String> handledEventIds = eventHandledDomainService.getEventIds(eventIdSet);

		// 이미 처리 된 이벤트 제외
		List<ProductMetricsCommand> unhandledCommands = commands.stream()
				.filter(command -> !handledEventIds.contains(command.eventId()))
				.toList();

		List<EventHandled> eventHandleds = unhandledCommands.stream()
				.map(command -> EventHandled.of(command.eventId()))
				.toList();

		Map<Long, List<ProductMetricsCommand>> groupedMetricsCommands = commands.stream()
				.filter(command -> !handledEventIds.contains(command.eventId()))
				.collect(Collectors.groupingBy(ProductMetricsCommand::ProductId));

		for (Map.Entry<Long, List<ProductMetricsCommand>> entry : groupedMetricsCommands.entrySet()) {
			Long productId = entry.getKey();
			List<ProductMetricsCommand> productMetricsCommands = entry.getValue();

			// ProductMetrics 조회
			ProductMetrics productMetrics = productMetricsDomainService.getProductMetrics(productId);

			for (ProductMetricsCommand command : productMetricsCommands) {
				// ProductMetrics 업데이트
				productMetricsDomainService.updateMetricsDeltas(productMetrics, MetricType.from(command.eventType()), command.delta());
			}

			// ProductMetrics 저장
			ProductMetrics saveMetrics = productMetricsDomainService.saveMetrics(productMetrics);

			// 랭킹 처리
			rankingService.updateRankingByMetrics(saveMetrics);
		}


		// 이벤트 핸들 저장
		eventHandledDomainService.saveEventHandledList(eventHandleds);

	}

}
