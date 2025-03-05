package com.polarbookshop.gateway_service.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;

import com.redis.testcontainers.RedisContainer;

public interface MyTestContainerConfig {
	
	@Container
	@ServiceConnection
	static RedisContainer redis = new RedisContainer("redis:7.4.2");

}
