package com.loopers.domain.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

class OrderCompleted {
	private final Long orderId;

	public OrderCompleted(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderId() {
		return orderId;
	}
}

@Component
class OrderApplicationPublisher {

	private final ApplicationEventPublisher applicationPublisher;

	@Autowired
	public OrderApplicationPublisher(ApplicationEventPublisher applicationPublisher) {
		this.applicationPublisher = applicationPublisher;
	}

	public void publish(OrderCompleted event) {
		applicationPublisher.publishEvent(event);
	}
}

@RecordApplicationEvents
@SpringBootTest
public class EventTest {
	@Autowired
	private OrderApplicationPublisher orderApplicationPublisher;

	@Autowired
	private ApplicationEvents applicationEvents;

	@Test
	void checkEvent() throws InterruptedException {
		// arrange
		OrderCompleted event = new OrderCompleted(1L);

		// act
		orderApplicationPublisher.publish(event);

		// assert
		Thread.sleep(1000); // 비동기 이벤트 리스너 고려 시 잠깐 대기

		System.out.println(applicationEvents.stream().toList()
				.stream()
				.map(Object::getClass)
				.toList());

		System.out.println(applicationEvents.stream(OrderCompleted.class).toList()
				.stream()
				.map(Object::getClass)
				.toList());
	}
}
