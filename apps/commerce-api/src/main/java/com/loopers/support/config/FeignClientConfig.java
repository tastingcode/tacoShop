package com.loopers.support.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

	@Bean
	public Request.Options feignOptions() {
		return new Request.Options(1000, 3000);
	}
}
