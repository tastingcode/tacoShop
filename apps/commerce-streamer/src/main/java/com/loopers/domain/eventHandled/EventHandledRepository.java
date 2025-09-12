package com.loopers.domain.eventHandled;

import java.util.List;
import java.util.Set;

public interface EventHandledRepository {
	boolean existsByEventId(String eventId);

	EventHandled save(EventHandled eventHandled);

	Set<String> findEventIdSet(Set<String> eventIds);

	List<EventHandled> saveAll(List<EventHandled> eventHandleds);
}
