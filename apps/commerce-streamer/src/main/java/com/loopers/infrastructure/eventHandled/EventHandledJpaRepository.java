package com.loopers.infrastructure.eventHandled;

import com.loopers.domain.eventHandled.EventHandled;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface EventHandledJpaRepository extends JpaRepository<EventHandled, String> {
	boolean existsByEventId(String eventId);

	List<EventHandled> findEventIdsByEventIdIn(Set<String> eventIds);

}
