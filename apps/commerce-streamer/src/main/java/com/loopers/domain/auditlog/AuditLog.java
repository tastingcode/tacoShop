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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String eventType;
	private String payload;
	@Column(name = "created_at", nullable = false, updatable = false)
	private ZonedDateTime createdAt;

	public static AuditLog of(String eventType, String payload) {
		AuditLog auditLog = new AuditLog();
		auditLog.eventType = eventType;
		auditLog.payload = payload;
		auditLog.createdAt = ZonedDateTime.now();
		return auditLog;
	}

}
