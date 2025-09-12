package com.loopers.domain.ranking;


import java.time.Duration;

public interface RankingRepository {

	void updateScore(String rankingKey, Long productId, double score);

	void setCacheExpiration(String rankingKey, Duration ttl);
}
