package com.loopers.domain.ranking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class RankingDomainService {

	private final RankingRepository rankingRepository;

	private static final String KEY_PREFIX = "ranking:all:";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	public String getDailyKey(String date){
		String key = date;
		if (date == null)
			key = LocalDate.now().format(DATE_FORMATTER);

		return KEY_PREFIX + key;
	}

	// 일단 금일 랭킹 기준 조회 TODO 주간, 월간 기준 등
	public Long getRankBy(Long productId){
		String rankingKey = KEY_PREFIX + LocalDate.now().format(DATE_FORMATTER);
		return rankingRepository.getRank(rankingKey, productId);
	}

	public Long getTotalCountBy(String rankingKey){
		return rankingRepository.findTotalCount(rankingKey);
	}

}
