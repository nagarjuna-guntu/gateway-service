package com.polarbookshop.gateway_service.web;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.polarbookshop.gateway_service.domain.Book;

import reactor.core.publisher.Mono;

@Configuration
public class WebEndpoints {

    @Bean
    RouterFunction<ServerResponse> catalogFallbackRoute() {
		return RouterFunctions.route()
					   .GET("/catalog-fallback", _ ->
							   ServerResponse.ok().body(Mono.just(Book.BOOK_CACHE), new ParameterizedTypeReference<List<Book>>(){}))
					   .POST("/catalog-fallback", _ ->
							   ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build())
					   .build();
						
	}

}
