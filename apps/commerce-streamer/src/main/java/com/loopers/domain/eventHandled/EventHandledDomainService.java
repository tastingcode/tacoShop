package com.loopers.domain.eventHandled;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EventHandledDomainService {
	private final EventHandledRepository eventHandledRepository;

	public boolean isHandled(String eventId) {
		return eventHandledRepository.existsByEventId(eventId);
	}

	public Set<String> getEventIds(Set<String> eventIdSet){
		return eventHandledRepository.findEventIds(eventIdSet);
	}

	public List<EventHandled> saveEventHandledList(List<EventHandled> eventHandledList) {
		return eventHandledRepository.saveAll(eventHandledList);
	}

	@Transactional
	public void saveEventHandled(String eventId) {
		eventHandledRepository.save(EventHandled.of(eventId));
	}
}
