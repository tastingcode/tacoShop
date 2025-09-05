package com.loopers.interfaces.consumer.auditlog;

import com.loopers.application.auditlog.AuditLogCommand;
import com.loopers.application.auditlog.AuditLogService;
import com.loopers.confg.kafka.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLogConsumer {

	private final AuditLogService auditLogService;

	@KafkaListener(
			topics = "${kafka.topic.audit-topic-name}",
			groupId = "${kafka.group.audit-topic-group-id}",
			containerFactory = KafkaConfig.STRING_BATCH_LISTENER
	)
	public void consume(List<ConsumerRecord<String, String>> records) {
		List<AuditLogCommand> commands = records.stream()
				.map(record -> {
					String payload = record.value();
					String eventType = new String(record.headers().lastHeader("eventType").value(), StandardCharsets.UTF_8);
					return AuditLogCommand.of(eventType, payload);
				})
				.toList();

		auditLogService.saveAuditLog(commands);
	}
}
