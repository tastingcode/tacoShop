package com.loopers.domain.event;

import org.apache.kafka.common.protocol.types.Field;

public interface DomainEvent {
	String eventId();
	
	default String getEventType() {
		return this.getClass().getSimpleName();
	}
}
