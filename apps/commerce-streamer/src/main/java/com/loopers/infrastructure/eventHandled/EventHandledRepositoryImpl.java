package com.loopers.infrastructure.eventHandled;

import com.loopers.domain.eventHandled.EventHandled;
import com.loopers.domain.eventHandled.EventHandledRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventHandledRepositoryImpl implements EventHandledRepository {

	private final EventHandledJpaRepository eventHandledJpaRepository;

	@Override
	public boolean existsByEventId(String eventId) {
		return eventHandledJpaRepository.existsByEventId(eventId);
	}

	@Override
	public EventHandled save(EventHandled eventHandled) {
		return eventHandledJpaRepository.save(eventHandled);
	}

	@Override
	public Set<String> findEventIdSet(Set<String> eventIds) {
		return eventHandledJpaRepository.findEventIdsByEventIdIn(eventIds).stream()
				.map(EventHandled::getEventId)
				.collect(Collectors.toSet());
	}

	@Override
	public List<EventHandled> saveAll(List<EventHandled> eventHandleds) {
		return eventHandledJpaRepository.saveAll(eventHandleds);
	}
}
