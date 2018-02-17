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
package com.example.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String signature;

    @Column(nullable = false)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.LAZY)
    @JoinTable(name = "user_authorities")
    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false)
    private Set<Authority> authorities;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public UserEntity(final String signature, final String username, final String password, final Set<Authority> authorities) {
        this.signature = signature;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserEntity of(final long id, final String signature, final String username, final String password, final Date createdAt, final Date updatedAt,
            final Authority... authorities) {
        final Set<Authority> authoritySet = Arrays.stream(authorities).collect(toSet());
        return new UserEntity(id, signature, username, password, authoritySet, createdAt, updatedAt);
    }

    @PrePersist
    public void setCreatedAt() {
        final ZonedDateTime now = ZonedDateTime.now();
        final Instant instant = now.toInstant();
        this.createdAt = Date.from(instant);
    }
}
