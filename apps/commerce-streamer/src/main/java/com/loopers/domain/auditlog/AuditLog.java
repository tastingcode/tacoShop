package com.loopers.domain.auditlog;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "event_log")
public class AuditLog {
	@Id
	@Column(name = "event_id", nullable = false, unique = true)
	private String eventId;
	private String eventType;
	private String payload;
	@Column(name = "created_at", nullable = false, updatable = false)
	private ZonedDateTime createdAt;

	public static AuditLog of(String eventId, String eventType, String payload) {
		AuditLog auditLog = new AuditLog();
		auditLog.eventId = eventId;
		auditLog.eventType = eventType;
		auditLog.payload = payload;
		auditLog.createdAt = ZonedDateTime.now();
		return auditLog;
	}

}
