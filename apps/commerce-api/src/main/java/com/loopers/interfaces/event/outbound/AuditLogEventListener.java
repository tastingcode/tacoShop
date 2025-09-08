package com.loopers.interfaces.event.outbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.domain.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLogEventListener {

	@Value( "${kafka.topic.audit-topic-name}")
	private String auditTopic;

	private final ObjectMapper objectMapper;
	private final KafkaTemplate<String, String> kafkaTemplate;


	@EventListener
	public void sendMessage(DomainEvent event){
		log.info("##### Audit Sending event: {}", event);
		try {
			String payload = objectMapper.writeValueAsString(event);
			String eventType = event.getEventType();
			String eventId = event.eventId();
			ProducerRecord<String, String> record = new ProducerRecord<>(auditTopic, payload);
			record.headers().add("eventType", eventType.getBytes(StandardCharsets.UTF_8));
			record.headers().add("eventId", eventId.getBytes(StandardCharsets.UTF_8));
			kafkaTemplate.send(record);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to serialize event");
		}
	}

}
