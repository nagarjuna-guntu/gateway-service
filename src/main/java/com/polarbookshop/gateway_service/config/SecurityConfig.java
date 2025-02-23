package com.polarbookshop.gateway_service.config;

import static org.springframework.security.config.Customizer.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestHandler;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.web.server.WebFilter;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	
	@Autowired
	private ReactiveClientRegistrationRepository reactiveClientRegistrationRepository;
	
	@Bean
	ServerOAuth2AuthorizedClientRepository sessionAuthorizedClientRepository() {
		return new WebSessionServerOAuth2AuthorizedClientRepository();
	}
	
	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		CookieServerCsrfTokenRepository tokenRepository = CookieServerCsrfTokenRepository.withHttpOnlyFalse();
		XorServerCsrfTokenRequestAttributeHandler delegate = new XorServerCsrfTokenRequestAttributeHandler();
		ServerCsrfTokenRequestHandler requestHandler = delegate::handle;
		http.authorizeExchange(authorize -> authorize
				.pathMatchers("/", "/*.css", "/*.js", "/favicon.ico").permitAll()
				.pathMatchers(HttpMethod.GET, "/books/**").permitAll()
				.pathMatchers("/actuator/**").permitAll()
				.anyExchange().authenticated())
		    .csrf(csrf -> csrf
		    	.csrfTokenRepository(tokenRepository)
		    	.csrfTokenRequestHandler(requestHandler))
		    .oauth2Login(withDefaults())
		    .exceptionHandling(exceptionHandling -> exceptionHandling
		    	.authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
		    .logout(logout -> logout
		    	.logoutSuccessHandler(oidcLogoutSuccessHandler()));
		return http.build();
		
	}

	private ServerLogoutSuccessHandler oidcLogoutSuccessHandler() {
		var oidcLogoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(this.reactiveClientRegistrationRepository);
		oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
		return oidcLogoutSuccessHandler;
	}
	
	@Bean
	WebFilter csrfWebFilter() {
		// Required because of https://github.com/spring-projects/spring-security/issues/5766		
		return (exchange, chain) -> {
	        Mono<CsrfToken> csrfToken = exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty());
	        return csrfToken.doOnSuccess(_ -> {
	            /* Ensures the token is subscribed to. */
	        }).then(chain.filter(exchange));
	    };
	}

}
