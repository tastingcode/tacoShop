package com.loopers.application.metrics;

import com.loopers.application.ranking.RankingService;
import com.loopers.domain.eventHandled.EventHandledDomainService;
import com.loopers.domain.metrics.MetricType;
import com.loopers.domain.metrics.ProductMetrics;
import com.loopers.domain.metrics.ProductMetricsDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProductMetricsService {

	private final EventHandledDomainService eventHandledDomainService;
	private final ProductMetricsDomainService productMetricsDomainService;
	private final RankingService rankingService;

	// TODO Refactor: MetricsFacade -> 1.isHandled() 2.saveMetrics() 3.updateRanking() 4.saveEventHandled()
	@Transactional
	public void handleMetrics(ProductMetricsCommand command) {
		// 이벤트 중복 처리 확인
		if (eventHandledDomainService.isHandled(command.eventId()))
			return;

		// ProductMetrics 조회
		ProductMetrics productMetrics = productMetricsDomainService.getProductMetrics(command.ProductId());

		// ProductMetrics 업데이트
		productMetricsDomainService.updateMetricsDeltas(productMetrics, MetricType.from(command.eventType()), command.delta());

		// ProductMetrics 저장
		productMetricsDomainService.saveMetrics(productMetrics);

		// 랭킹 처리
		rankingService.updateRanking(command.ProductId(), MetricType.from(command.eventType()));

		// 이벤트 핸들 처리
		eventHandledDomainService.saveEventHandled(command.eventId());
	}

}
