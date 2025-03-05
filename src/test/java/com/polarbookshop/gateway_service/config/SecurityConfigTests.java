package com.polarbookshop.gateway_service.config;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOidcLogin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@WebFluxTest
@Import(SecurityConfig.class)
public class SecurityConfigTests {
	
	@Autowired
	private WebTestClient webClient;
	
	@MockitoBean
	private ReactiveClientRegistrationRepository clientRegistrationRepository;
	

	@Test
	void whenOIDCLoogoutNotAuthebticatedAndNoCsrfTokenThen403() {
		webClient
			.post()
			.uri("/logout")
			.exchange()
			.expectStatus().isForbidden();
		
	}
	
	@Test
	void whenOIDCLoogoutAuthebticatedAndNoCsrfTokenThen403() {
		webClient
			.mutateWith(mockOidcLogin())
			.post()
			.uri("/logout")
			.exchange()
			.expectStatus().isForbidden();
		
	}
	
	@Test
	void whenOIDCLogoutAuthenticatedAndWithCsrfEnabledThen302Redirect(){
		when(clientRegistrationRepository.findByRegistrationId("test")).thenReturn(Mono.just(mockClientRegistration()));
		webClient
			.mutateWith(mockOidcLogin())
			.mutateWith(csrf())
			.post()
			.uri("/logout")
			.exchange()
			.expectStatus().is3xxRedirection();
		
	}
	
	private ClientRegistration mockClientRegistration() {
		return ClientRegistration.withRegistrationId("test")
		 				  		 .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
		 				  		 .clientId("test")
		 				  		 .authorizationUri("https://sso.polarbookshop.com/auth")
		 				  		 .tokenUri("https://sso.polarbookshop.com/token")
		 				  		 .redirectUri("https://polarbookshop.com")
		 				  		 .build();
	}

}
