package com.loopers.application.eventHandled;

import com.loopers.application.metrics.ProductMetricsCommand;
import com.loopers.domain.eventHandled.EventHandled;
import com.loopers.domain.eventHandled.EventHandledDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventHandledService {

	private final EventHandledDomainService eventHandledDomainService;

	public Set<String> getAlreadyHandledEventIdSet(List<ProductMetricsCommand> commands){
		Set<String> eventIdSet = commands.stream()
				.map(ProductMetricsCommand::eventId)
				.collect(Collectors.toSet());

		return eventHandledDomainService.getEventSet(eventIdSet);
	}

	public List<EventHandled> extractUnhandledEventHandledList(List<ProductMetricsCommand> commands, Set<String> eventIdSet) {
		return commands.stream()
				.filter(command -> !eventIdSet.contains(command.eventId()))
				.map(command -> EventHandled.of(command.eventId()))
				.toList();
	}

}
