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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
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

    @Column(nullable = false)
    private String password;

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

    @PrePersist
    public void setCreatedAt() {
        final ZonedDateTime now = ZonedDateTime.now();
        final Instant instant = now.toInstant();
        this.createdAt = Date.from(instant);
    }
}
