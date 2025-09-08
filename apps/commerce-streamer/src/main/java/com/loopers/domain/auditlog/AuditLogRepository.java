package com.loopers.domain.auditlog;

import java.util.List;

public interface AuditLogRepository {
	AuditLog save(AuditLog auditLog);
	List<AuditLog> saveAll(List<AuditLog> auditLogs);
}
