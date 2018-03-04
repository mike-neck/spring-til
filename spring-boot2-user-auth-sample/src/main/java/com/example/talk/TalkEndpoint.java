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
package com.example.talk;

import com.example.model.TalkRoom;
import com.example.user.UserAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TalkEndpoint {

    private final TalkRoomRepository talkRoomRepository;

    public TalkEndpoint(final TalkRoomRepository talkRoomRepository) {
        this.talkRoomRepository = talkRoomRepository;
    }

    Mono<ServerResponse> getTalkRoom(final ServerRequest serverRequest) {
        final long talkId = Long.parseLong(serverRequest.pathVariable("talkId"));
        final Mono<TalkRoom> talkRoom = talkRoomRepository.getTalkRoomByTalkRoomId(talkId);
        return talkRoom.flatMap(tr -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(tr), TalkRoom.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    Mono<ServerResponse> all(final ServerRequest request) {
        return request.principal()
                .cast(UsernamePasswordAuthenticationToken.class)
                .map(UsernamePasswordAuthenticationToken::getPrincipal)
                .cast(UserAdapter.class)
                .map(UserAdapter::getUserId)
                .map(talkRoomRepository::findTalkRoomByUserId)
                .flatMap(Flux::collectList)
                .flatMap(list -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(list),
                        new ParameterizedTypeReference<List<TalkRoom>>() {
                        }))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @Bean
    RouterFunction<ServerResponse> talkEndpointHandler() {
        return route(GET("/talks/{talkId}"), this::getTalkRoom)
                .andRoute(GET("/talks"), this::all);
    }
}
