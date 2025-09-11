package com.loopers.infrastructure.ranking;

import com.loopers.domain.ranking.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepository {
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public void updateScore(String rankingKey, Long productId, double score) {
		redisTemplate.opsForZSet().incrementScore(rankingKey, productId.toString(), score);
	}

	@Override
	public void setCacheExpiration(String rankingKey, Duration ttl) {
		redisTemplate.expire(rankingKey, ttl);
	}


}
