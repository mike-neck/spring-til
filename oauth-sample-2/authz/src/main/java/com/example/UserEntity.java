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
package com.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    private Long userId;
    private String username;
    private String password;
    private Set<String> authorities;

    public Set<GrantedAuthority> getGrantedAuthorities() {
        return authorities.stream()
                .map(a -> (GrantedAuthority) () -> a)
                .collect(toSet());
    }

    @Data
    @RequiredArgsConstructor
    public static class WithoutPassword {
        private final Long userId;
        private final String username;
        private final Set<String> authorities;
    }
}
