package com.loopers.infrastructure.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.domain.product.ProductCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ProductCacheRepositoryImpl implements ProductCacheRepository {
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public String get(String cacheKey) {
		return redisTemplate.opsForValue().get(cacheKey);
	}

	@Override
	public <T> void set(String key, T value, Duration ttl) throws JsonProcessingException {
		String json = objectMapper.writeValueAsString(value);
		redisTemplate.opsForValue().set(key, json, ttl);
	}

	@Override
	public <T> void set(String key, T value) throws JsonProcessingException {
		set(key, value, Duration.ofMinutes(5));
	}


}
