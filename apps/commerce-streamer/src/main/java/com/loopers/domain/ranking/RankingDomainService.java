package com.loopers.domain.ranking;

import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class RankingDomainService {

	private static final String KEY_PREFIX = "ranking:all:";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	public String getDailyKey(){
		String today = LocalDate.now().format(DATE_FORMATTER);
		return KEY_PREFIX + today;
	}

}
