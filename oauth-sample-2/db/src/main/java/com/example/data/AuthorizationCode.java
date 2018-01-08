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
package com.example.data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "authorization_codes")
public class AuthorizationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id")
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "code")
    private String authorizationCode;

    @ElementCollection(fetch = FetchType.LAZY, targetClass = Scope.class)
    @JoinTable(name = "authorized_scopes", joinColumns = @JoinColumn(name = "auth_code_id", nullable = false))
    @Column(name = "scope", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Scope> scopes;

    private LocalDateTime created;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(final Application application) {
        this.application = application;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(final String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(final LocalDateTime created) {
        this.created = created;
    }

    public Set<Scope> getScopes() {
        return scopes;
    }

    public void setScopes(final Set<Scope> scopes) {
        this.scopes = scopes;
    }

    public AuthorizationCode(final Application application, final User user, final String authorizationCode, final Set<Scope> scopes) {
        this.application = application;
        this.user = user;
        this.authorizationCode = authorizationCode;
        this.scopes = scopes;
    }

    public AuthorizationCode() {
    }

    @PrePersist
    public void onCreate() {
        this.created = LocalDateTime.now();
    }
}
