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
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableWebFlux
@Slf4j
public class Application {

    private static final String ACTION = "action";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public Application(final StateMachine<IssueStates, UserAction> stateMachine) {
        this.stateMachine = stateMachine;
    }

    private final StateMachine<IssueStates, UserAction> stateMachine;

    Mono<ServerResponse> stateChange(final ServerRequest request) {
        log.info("coming event: {}(current: {})", request.pathVariable(ACTION), stateMachine.getState().getId());
        final String action = request.pathVariable(ACTION).toUpperCase();
        final Optional<UserAction> userAction = UserAction.find(action);
        return userAction
                .filter(stateMachine::sendEvent)
                .map(ua -> new ActionResult(ua, stateMachine.getState().getId()))
                .map(Mono::just)
                .map(result -> ServerResponse.accepted().body(result, ActionResult.class))
                .orElseGet(() -> ServerResponse.notFound().build());
    }

    Mono<ServerResponse> showState(final ServerRequest request) {
        final IssueStates currentState = stateMachine.getState().getId();
        log.info("current state: {}", currentState);
        return ServerResponse.ok()
                .body(Mono.just(new ShowState(currentState)), ShowState.class);
    }

    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return route(
                POST("/states/{action}"), this::stateChange
        ).andRoute(
                GET("/states"), this::showState
        );
    }

    @Value
    public static class ShowState {
        private final IssueStates currentStates;
    }

    @Value
    public static class ActionResult {
        private final UserAction action;
        private final IssueStates currentStates;
    }
}
