package com.loopers.application.metrics;

import com.loopers.application.eventHandled.EventHandledService;
import com.loopers.domain.eventHandled.EventHandled;
import com.loopers.domain.eventHandled.EventHandledDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MetricsFacade {

	private final EventHandledService eventHandledService;
	private final ProductMetricsService productMetricsService;
	private final EventHandledDomainService eventHandledDomainService;

	@Transactional
	public void processMetrics(List<ProductMetricsCommand> commands) {
		// 이벤트 핸들 ID 조회
		Set<String> eventIdSet = eventHandledService.getAlreadyHandledEventIdSet(commands);

		// 처리되지 않은 이벤트 핸들 목록 추출
		List<EventHandled> eventHandledList = eventHandledService.extractUnhandledEventHandledList(commands, eventIdSet);

		// 집계 및 랭킹 업데이트
		productMetricsService.updateMetricsAndRanking(commands, eventIdSet);

		// 이벤트 핸들 저장
		eventHandledDomainService.saveEventHandledList(eventHandledList);
	}

}
