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

import com.example.HalJson;
import com.example.Paging;
import com.example.entity.MessageEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "messages", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MessageEndpoint {

    private static final String MESSAGES_END_POINT = "http://localhost:5000/db-app/messages";

    private final RestTemplate restTemplate;

    public MessageEndpoint(@HalJson final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    Paging<MessageJson> getMessages(@RequestParam(value = "size", required = false) final Integer size,
            @RequestParam(value = "page", required = false) final Integer page) {
        final String url = String.format("%s?size=%d&page=%d", MESSAGES_END_POINT, size == null ? 5 : size, page == null ? 0 : page);
        final ResponseEntity<PagedResources<Resource<MessageEntity>>> entity = restTemplate.exchange(url, HttpMethod.GET, null,
                new TypeReferences.PagedResourcesType<Resource<MessageEntity>>() {});
        final PagedResources<Resource<MessageEntity>> body = entity.getBody();
        final List<MessageJson> messages = body
                .getContent()
                .stream()
                .map(r -> new MessageEntity.WithLink(r.getContent(), r.getLink("self").getHref()))
                .map(MessageEntity.WithLink::toJson)
                .collect(toList());
        final PagedResources.PageMetadata metadata = body.getMetadata();
        return new Paging<>(messages, ((int) metadata.getSize()), metadata.getTotalElements(), ((int) metadata.getTotalPages()), ((int) metadata.getNumber()));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    MessageJson createMessage(@RequestParam(name = "text") final String text, @RequestParam(name = "user_id") final Long userId) {
        Objects.requireNonNull(text);
        Objects.requireNonNull(userId);
        final MessageEntity.Request messageEntity = new MessageEntity.Request(text, userId);
        final ResponseEntity<Resource<MessageEntity>> responseEntity = postMessageEntityToDbApp(messageEntity);
        return MessageEntity.withLink(responseEntity).toJson();
    }

    private ResponseEntity<Resource<MessageEntity>> postMessageEntityToDbApp(final MessageEntity.Request messageEntity) {
        final RequestEntity<MessageEntity.Request> request = RequestEntity.post(URI.create(MESSAGES_END_POINT))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(messageEntity);
        return restTemplate.exchange(MESSAGES_END_POINT, HttpMethod.POST, request,
                new ParameterizedTypeReference<Resource<MessageEntity>>() {});
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE })
    MessageJson createMessage(@RequestBody final MessageJson.Request request) {
        Objects.requireNonNull(request);
        final ResponseEntity<Resource<MessageEntity>> response = postMessageEntityToDbApp(request.toEntity());
        return MessageEntity.withLink(response).toJson();
    }
}
