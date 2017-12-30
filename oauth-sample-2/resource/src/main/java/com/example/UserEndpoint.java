/*
 * Copyright 2017 Shinya Mochida
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

import com.example.entity.UserEntity;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserEndpoint {

    private final RestTemplate restTemplate;

    public UserEndpoint(@HalJson final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    List<UserJson> getUsers() {
        final ResponseEntity<PagedResources<Resource<UserEntity>>> entity =
                restTemplate.exchange("http://localhost:5000/db-app/users",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<PagedResources<Resource<UserEntity>>>() {});
        return entity.getBody()
                .getContent()
                .stream()
                .map(r -> new UserEntity.Resource(r.getContent().getName(), r.getLink("self").getHref()))
                .map(UserEntity.Resource::toJson)
                .collect(toList());
    }

    public static void main(String[] args) {
        final ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jackson2HalModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        converter.setSupportedMediaTypes(Collections.singletonList(MediaTypes.HAL_JSON));
        final RestTemplate restTemplate = new RestTemplate(Collections.singletonList(converter));
        final ResponseEntity<PagedResources<Resource<UserEntity>>> entity =
                restTemplate.exchange("http://localhost:5000/db-app/users",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<PagedResources<Resource<UserEntity>>>() {});
        System.out.println(entity.getStatusCode());
        final PagedResources<Resource<UserEntity>> body = entity.getBody();
        System.out.println(body);
        final Collection<Resource<UserEntity>> contents = body.getContent();
        final List<UserEntity> userEntities = contents.stream()
                .map(Resource::getContent)
                .collect(toList());
    }
}
