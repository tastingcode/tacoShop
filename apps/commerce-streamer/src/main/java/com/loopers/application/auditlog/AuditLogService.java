package com.loopers.application.auditlog;

import com.loopers.domain.auditlog.AuditLog;
import com.loopers.domain.auditlog.AuditLogRepository;
import com.loopers.domain.eventHandled.EventHandled;
import com.loopers.domain.eventHandled.EventHandledRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuditLogService {

	private final EventHandledRepository eventHandledRepository;
	private final AuditLogRepository auditLogRepository;

	@Transactional
	public void saveAuditLog(List<AuditLogCommand> commands) {
		// 이벤트 핸들 ID 조회
		Set<String> handledEventIds = eventHandledRepository.findEventIds(commands.stream()
				.map(AuditLogCommand::eventId)
				.collect(Collectors.toSet()));

		// 이미 처리 된 이벤트 제외한 감사로그 생성
		List<AuditLog> auditLogs = commands.stream()
				.filter(command -> !handledEventIds.contains(command.eventId()))
				.map(command -> AuditLog.of(command.eventId(), command.eventType(), command.payload()))
				.toList();

		// 이벤트 핸들 리스트 생성
		List<EventHandled> eventHandleds = auditLogs.stream()
				.map(log -> EventHandled.of(log.getEventId()))
				.toList();

		// 감사로그 저장
		auditLogRepository.saveAll(auditLogs);

		// 이벤트 핸들 저장
		eventHandledRepository.saveAll(eventHandleds);
	}
}
