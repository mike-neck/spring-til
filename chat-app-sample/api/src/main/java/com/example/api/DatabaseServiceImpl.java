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
package com.example.api;

import com.example.api.data.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class DatabaseServiceImpl {

    private final RestTemplate restTemplate;

    public DatabaseServiceImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @NotNull
    public Optional<Message> getMessageById(final long messageId) {
        try {
            final ResponseEntity<Message> entity = restTemplate.getForEntity("http://localhost:5000/messageEntity/" + messageId, Message.class);
            if (entity.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(entity.getBody());
            }
            return Optional.empty();
        } catch (RestClientException e) {
            return Optional.empty();
        }
    }
}
