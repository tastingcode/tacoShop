package com.loopers.application.auditlog;

import com.loopers.domain.auditlog.AuditLog;
import com.loopers.domain.auditlog.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuditLogService {

	private final AuditLogRepository auditLogRepository;

	@Transactional
	public void saveAuditLog(List<AuditLogCommand> commands) {
		List<AuditLog> auditLogs = commands.stream()
				.map(command -> AuditLog.of(command.eventType(), command.payload()))
				.toList();

		auditLogRepository.saveAll(auditLogs);
	}
}
