package com.loopers.domain.product;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.Duration;
import java.util.List;

public interface ProductCacheRepository {
	String get(String cacheKey);

	<T> void set(String key, T value, Duration ttl) throws JsonProcessingException;

	<T> void set(String key, T value) throws JsonProcessingException;

	void delete(List<String> keys);
}
