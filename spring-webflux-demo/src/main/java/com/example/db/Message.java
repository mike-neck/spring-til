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
package com.example.db;

import lombok.Value;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Table
@Value
public class Message implements Comparable<Message> {

    @PrimaryKey
    private final Long id;

    @Indexed
    private final String userId;

    @Indexed
    private final String text;

    @Indexed
    private final LocalDateTime created;

    public static Message createNew(final User user, final String text) {
        final long id = System.currentTimeMillis();
        return new Message(id, user.getId(), text, LocalDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public int compareTo(final Message o) {
        return id.compareTo(o.id);
    }
}
