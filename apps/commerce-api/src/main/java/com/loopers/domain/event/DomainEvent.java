package com.loopers.domain.event;

public interface DomainEvent {
	default String getEventType() {
		return this.getClass().getSimpleName();
	}
}
