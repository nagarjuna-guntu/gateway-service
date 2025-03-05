package com.polarbookshop.gateway_service.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClientConfigurer;

import com.polarbookshop.gateway_service.config.SecurityConfig;
import com.polarbookshop.gateway_service.domain.User;
import com.polarbookshop.gateway_service.web.UserController;

@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {
	
	@Autowired
	private WebTestClient webClient;
	
	@MockitoBean
	private ReactiveClientRegistrationRepository clientRegistrationRepository;

	
	@Test
	public void status401WhenNotAuthenticated() throws Exception {
		this.webClient
			.get()
			.uri("/user")
			.exchange()
			.expectStatus().isUnauthorized();
	}
	
	@Test
	public void returnUserWhenAuthenticated() {
		 var expectedUser = new User("jon.snow", "Jon", "Snow",
				    List.of("employee", "customer"));
		
		this.webClient
	        .mutateWith(configureMockOidcLogin(expectedUser))
	        .get()
	        .uri("/user")
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(User.class)
			.value(user -> assertThat(user).isEqualTo(expectedUser));
	}

	private WebTestClientConfigurer configureMockOidcLogin(User expectedUser) {		
		return SecurityMockServerConfigurers
				.mockOidcLogin()
				.idToken(token -> {
					token.claim(StandardClaimNames.PREFERRED_USERNAME, expectedUser.username());
					token.claim(StandardClaimNames.GIVEN_NAME, expectedUser.firstName());
					token.claim(StandardClaimNames.FAMILY_NAME, expectedUser.lastName());
					token.claim("roles", expectedUser.roles());
				});
	}

}
