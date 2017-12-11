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
package com.example.database.data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime created;

    @PrePersist
    public void setCurrentTime() {
        setCreated(LocalDateTime.now(ZoneId.of("Asia/Tokyo")));
    }

    @SuppressWarnings("unused")
    public MessageEntity() {
    }

    @SuppressWarnings("unused")
    public MessageEntity(final String username, final String message) {
        this.username = username;
        this.message = message;
    }

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(final Long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getUsername() {
        return username;
    }

    @SuppressWarnings("unused")
    public void setUsername(final String username) {
        this.username = username;
    }

    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }

    @SuppressWarnings("unused")
    public void setMessage(final String message) {
        this.message = message;
    }

    @SuppressWarnings("unused")
    public LocalDateTime getCreated() {
        return created;
    }

    @SuppressWarnings("WeakerAccess")
    public void setCreated(final LocalDateTime created) {
        this.created = created;
    }
}
