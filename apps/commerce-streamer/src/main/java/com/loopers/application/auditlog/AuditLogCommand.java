package com.loopers.application.auditlog;

public record AuditLogCommand(
		String eventId,
		String eventType,
		String payload
) {
	public static AuditLogCommand of(String eventId, String eventType, String payload) {
		return new AuditLogCommand(eventId, eventType, payload);
	}
}
