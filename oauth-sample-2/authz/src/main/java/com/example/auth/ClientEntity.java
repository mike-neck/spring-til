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
package com.example.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {

    private String clientId;
    private String clientSecret;
    private Set<String> scopes;
    private Set<String> authority;
    private String redirectUri;

    public Immutable toImmutable() {
        return new Immutable(clientId,
                clientSecret,
                Collections.unmodifiableSet(scopes),
                Collections.unmodifiableSet(authority),
                redirectUri);
    }

    @Data
    @RequiredArgsConstructor
    public static class Immutable {
        private final String clientId;
        private final String clientSecret;
        private final Set<String> scopes;
        private final Set<String> authorities;
        private final String redirectUri;

        public Set<GrantedAuthority> authorityAsFrameworkType() {
            return authorities.stream()
                    .map(TO_GRANTED_AUTHORITY)
                    .collect(toSet());
        }
    }

    private static final Function<String, GrantedAuthority> TO_GRANTED_AUTHORITY = s -> () -> s;
}
