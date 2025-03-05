package com.polarbookshop.gateway_service.domain;

import java.util.List;

public record User(
		String username,
		String firstName,
		String lastName,
		List<String> roles
	) {}
