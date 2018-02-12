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
package com.example.user;

import com.example.auth.AuthenticatedUser;
import com.example.db.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserEndpoint {

    private final UserService userService;

    public UserEndpoint(final UserService userService) {
        this.userService = userService;
    }

    @Bean
    RouterFunction<ServerResponse> userEndpointRouterFunction() {
        return route(GET("/me"), this::me);
    }

    private Mono<ServerResponse> me(final ServerRequest request) {
        final Mono<User> user = request.principal()
                .cast(UsernamePasswordAuthenticationToken.class)
                .map(UsernamePasswordAuthenticationToken::getPrincipal)
                .cast(AuthenticatedUser.class)
                .log("/users/me")
                .map(AuthenticatedUser::getUser);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(user, User.class);
    }
}
