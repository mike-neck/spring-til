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
package com.example.model;

import lombok.Value;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Sets;

import java.time.LocalDateTime;

@Value
public class UserAccount {

    private Long id;
    private String loginId;
    private String password;
    private String displayName;
    private LocalDateTime created;

    private ImmutableSet<Role> roles;

    public UserAccount(long id, String loginId, String password, String displayName, Role... roles) {
        final LocalDateTime now = LocalDateTime.now();
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.displayName = displayName;
        this.created = now;
        this.roles = Sets.immutable.of(roles);
    }

    public static UserAccount of(long id, String loginId, String password, String displayName, Role... roles) {
        return new UserAccount(id, loginId, password, displayName, roles);
    }
}
