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
package com.example.entity;

import com.example.message.MessageJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {

    private String text;
    private LocalDateTime created;
    private Long userId;

    @Data
    @RequiredArgsConstructor
    public static class WithLink {
        private final String  text;
        private final Long userId;
        private final LocalDateTime created;
        private final String selfUrl;

        public WithLink (final MessageEntity entity, String selfUrl) {
            this (entity.text, entity.userId, entity.created, selfUrl);
        }

        public MessageJson toJson() {
            final int index = selfUrl.lastIndexOf('/');
            final String id = selfUrl.substring(index + 1);
            return new MessageJson(Long.getLong(id), userId, text, created);
        }
    }
}
