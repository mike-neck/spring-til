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

import com.example.db.Message;
import com.example.util.PageResult;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.util.Collection;
import java.util.Comparator;

public class GetMessageRequest {

    private final int page;
    private final int size;

    private GetMessageRequest(final int page, final int size) {
        this.page = page;
        this.size = size;
    }

    Flux<Message> filterPages(final Flux<Message> messages) {
        return messages.sort(Comparator.reverseOrder())
                .index()
                .filter(tuple -> tuple.getT1() >= page * size)
                .take(size)
                .map(Tuple2::getT2);
    }

    PageResult<Message> toResult(final Collection<Message> messages) {
        final int size = messages.size();
        return new PageResult<>(messages, page, size);
    }

    static GetMessageRequest create(final ServerRequest request) {
        final Integer page = request.queryParam("page").map(Integer::parseInt).orElse(0);
        final Integer size = request.queryParam("size").map(Integer::parseInt).orElse(20);
        return new GetMessageRequest(page, size);
    }
}
