package com.polarbookshop.gateway_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.polarbookshop.gateway_service.config.MyTestContainerConfig;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@ImportTestcontainers(MyTestContainerConfig.class)
class GatewayServiceApplicationTests {
	
	@MockitoBean
	private ReactiveClientRegistrationRepository clientRegistrationRepository;
	
	@Test
	void contextLoads() {
	}

}
