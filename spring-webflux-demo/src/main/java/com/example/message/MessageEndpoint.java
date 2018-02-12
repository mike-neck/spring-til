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
package com.example.message;

import com.example.auth.AuthenticatedUser;
import com.example.db.Message;
import com.example.db.User;
import com.example.util.PageResult;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MessageEndpoint {

    private final MessageRepository messageRepository;

    public MessageEndpoint(final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Bean
    RouterFunction<ServerResponse> messageEndpointRouterFunction() {
        return nest(path("/messages"),
                route(POST("/"), this::createMessage)
                        .andRoute(GET("/"), this::getMessages)
        );
    }

    private Mono<ServerResponse> getMessages(final ServerRequest request) {
        final GetMessageRequest getMessageRequest = GetMessageRequest.create(request);
        final Mono<PageResult<Message>> result = messageRepository.findAll()
                .as(getMessageRequest::filterPages)
                .collectList()
                .map(getMessageRequest::toResult);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result, new ParameterizedTypeReference<PageResult<Message>>() {});
    }

    private Mono<ServerResponse> createMessage(final ServerRequest request) {
        final Mono<Message> messageMono = request.principal()
                .cast(UsernamePasswordAuthenticationToken.class)
                .map(UsernamePasswordAuthenticationToken::getPrincipal)
                .cast(AuthenticatedUser.class)
                .map(AuthenticatedUser::getUser)
                .flatMap(user -> request.bodyToMono(TextMessage.class).map(message -> message.toMessage(user)))
                .flatMap(messageRepository::save);
        return messageMono.map(message -> request.uriBuilder().path("/messages/{message_id}").build(message.getId()))
                .map(ServerResponse::created)
                .flatMap(res -> res.body(messageMono, Message.class));
    }

    @Data
    public static class TextMessage {
        private String text;

        private Message toMessage(final User user) {
            return Message.createNew(user, text);
        }
    }
}
