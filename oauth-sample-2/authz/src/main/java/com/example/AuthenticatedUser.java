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

import org.springframework.security.core.userdetails.User;

import java.util.Set;

public class AuthenticatedUser extends User {

    private final Set<String> authorities;
    private Long userId;

    AuthenticatedUser(final UserEntity userEntity) {
        super(userEntity.getUsername(), userEntity.getPassword(), userEntity.getGrantedAuthorities());
        this.userId = userEntity.getUserId();
        this.authorities = userEntity.getAuthorities();
    }

    public Set<String> getAuthorityStrings() {
        return authorities;
    }

    public Long getUserId() {
        return userId;
    }
}
