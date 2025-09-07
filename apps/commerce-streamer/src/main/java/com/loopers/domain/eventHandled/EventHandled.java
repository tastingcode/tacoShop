package com.loopers.domain.eventHandled;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "event_handled")
public class EventHandled {
	@Id
	@Column(name = "event_id", nullable = false, unique = true)
	private String eventId;

	@Column(name = "processed_at")
	private LocalDateTime processedAt;

	public static EventHandled of(String eventId) {
		EventHandled eventHandled = new EventHandled();
		eventHandled.eventId = eventId;
		eventHandled.processedAt = LocalDateTime.now();
		return eventHandled;
	}
}
