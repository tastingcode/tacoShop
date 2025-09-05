package com.loopers.application.auditlog;

public record AuditLogCommand(
		String eventType,
		String payload
) {
	public static AuditLogCommand of(String eventType, String payload) {
		return new AuditLogCommand(eventType, payload);
	}
}
