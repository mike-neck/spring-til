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
import java.time.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AccessTokenEntity {

    @EmbeddedId
    private AccessTokenId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_application_id", nullable = false, insertable = false, updatable = false)
    private ClientApplicationEntity clientApplication;

    @Column(nullable = false, name = "access_token")
    private String accessToken;

    @Column(nullable = false, name = "refresh_token")
    private String refreshToken;

    @ElementCollection(fetch = FetchType.LAZY)
    @JoinTable(name = "access_token_scopes")
    @Column(name = "scope", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Scope> scopes;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiration;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Version
    private Integer version;

    public AccessTokenEntity(final UserEntity user, final ClientApplicationEntity clientApplication, final String accessToken, final String refreshToken,
            final Set<Scope> scopes) {
        this.user = user;
        this.clientApplication = clientApplication;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.scopes = scopes;
        updateId();
    }

    public AccessTokenEntity(final UserEntity user, final ClientApplicationEntity clientApplication, final String accessToken, final String refreshToken,
            final Set<Scope> scopes, final OffsetDateTime createdAt) {
        this.user = user;
        this.clientApplication = clientApplication;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.scopes = scopes;
        this.createdAt = Date.from(createdAt.toInstant());
        updateId();
    }

    @PrePersist
    public void created() {
        final OffsetDateTime now = nowOnCreation();
        created(now);
        update(now);
        expiration(now);
    }

    private OffsetDateTime nowOnCreation() {
        if (this.createdAt == null) {
            return OffsetDateTime.now();
        } else {
            final ZonedDateTime zonedDateTime = this.createdAt.toInstant().atZone(ZoneId.systemDefault());
            return OffsetDateTime.from(zonedDateTime);
        }
    }

    private void created(final OffsetDateTime now) {
        this.createdAt = Date.from(now.toInstant());
    }

    private void update(final OffsetDateTime now) {
        this.updatedAt = Date.from(now.toInstant());
    }

    private void expiration(final OffsetDateTime now) {
        final OffsetDateTime expired = now.plusDays(2L);
        this.expiration = Date.from(expired.toInstant());
    }

    @PreUpdate
    public void updated() {
        update(OffsetDateTime.now());
    }

    private void updateId() {
        if (this.id == null) {
            this.id = new AccessTokenId();
        }
        if (this.user != null) {
            this.id.setUserId(this.user.getId());
        }
        if (this.clientApplication != null) {
            this.id.setClientApplicationId(this.clientApplication.getId());
        }
    }

    public void setUser(final UserEntity user) {
        this.user = user;
        updateId();
    }

    public void setClientApplication(final ClientApplicationEntity clientApplication) {
        this.clientApplication = clientApplication;
        updateId();
    }
}
