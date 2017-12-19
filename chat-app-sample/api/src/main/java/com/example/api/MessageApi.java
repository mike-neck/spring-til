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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "message")
public class MessageApi {

    private final DatabaseServiceImpl databaseService;

    public MessageApi(final DatabaseServiceImpl databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping("{id}")
    ResponseEntity<Message> getMessage(@PathVariable("id") long messageId) {
        final Optional<Message> messageById = databaseService.getMessageById(messageId);
        return messageById.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
