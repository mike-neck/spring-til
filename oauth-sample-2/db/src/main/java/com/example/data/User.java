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
package com.example.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({ "password", "authorities" })
public class User implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    @ElementCollection(targetClass = Authority.class)
    @JoinTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id", nullable = false))
    @Column(name = "authority", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Authority> authorities;

    public User() {
    }

    public User(final String name, final String password) {
        this.name = name;
        this.password = password;
        this.authorities = Authority.defaultSet();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(final Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<String> getAuthoritiesAsString() {
        return authorities.stream()
                .map(Enum::name)
                .collect(toSet());
    }
}
