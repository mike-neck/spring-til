package com.example;

import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableWebFlux
@EnableWebFluxSecurity
public class SpringWebfluxDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebfluxDemoApplication.class, args);
    }

    @Bean
    MapReactiveUserDetailsService userDetailsService() {
        final UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("foo")
                .password("bar")
                .roles("BAZ", "QUX")
                .build();
        return new MapReactiveUserDetailsService(userDetails);
    }

    @Bean
    RouterFunction<ServerResponse> routerFunction(final SpringWebfluxDemoApplication springWebfluxDemoApplication) {
        return route(GET("/hello"), springWebfluxDemoApplication::hello);
    }

    Mono<ServerResponse> hello(final ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Message("hello", OffsetDateTime.now(ZoneId.of("Z")))), Message.class);
    }

    @Value
    public static class Message {
        final String text;
        final OffsetDateTime time;
    }
}
