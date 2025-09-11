package com.loopers.infrastructure.ranking;

import com.loopers.domain.ranking.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepository {
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public List<Long> getRankingProductIds(String rankingKey, long start, long end) {
		return redisTemplate.opsForZSet().reverseRange(rankingKey, start, end).stream()
				.map(Long::valueOf)
				.toList();
	}

	@Override
	public Long getRank(String rankingKey, Long productId) {
		Long rank = redisTemplate.opsForZSet().reverseRank(rankingKey, productId.toString());
		return rank != null ? rank + 1 : null;
	}

}
