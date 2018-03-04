/*
 * Copyright 2018 Shinya Mochida
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example;

import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TestEndpoint {

  Mono<ServerResponse> test(final ServerRequest request) {
    final Optional<String> value = request.queryParam("value");
    return Mono.justOrEmpty(value)
        .map(Wrapper::new)
        .flatMap(v -> ServerResponse.ok().body(Mono.just(v), Wrapper.class))
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  @Bean
  RouterFunction<ServerResponse> testEndpointHandler() {
    return route(GET("/test"), this::test);
  }

  @Value
  public static class Wrapper {
    private final String value;
  }
}
