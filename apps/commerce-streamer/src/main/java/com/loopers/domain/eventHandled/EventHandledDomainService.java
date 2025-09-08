package com.loopers.domain.eventHandled;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EventHandledDomainService {
	private final EventHandledRepository eventHandledRepository;

	public boolean isHandled(String eventId) {
		return eventHandledRepository.existsByEventId(eventId);
	}

	@Transactional
	public void saveEventHandled(String eventId) {
		eventHandledRepository.save(EventHandled.of(eventId));
	}
}
