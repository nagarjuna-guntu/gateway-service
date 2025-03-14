package com.polarbookshop.gateway_service.web;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarbookshop.gateway_service.domain.User;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class UserController {
	
	@GetMapping("user")
	public Mono<User> getUser(@AuthenticationPrincipal OidcUser oidcUser) {
		log.info("Fetching information about the currently authenticated user");
		var user = new User(oidcUser.getPreferredUsername(), 
						 oidcUser.getGivenName(), 
						 oidcUser.getFamilyName(), 
						 oidcUser.getClaimAsStringList("roles"));
		return Mono.just(user);
		
	}

}
