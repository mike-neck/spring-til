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
package com.example.user;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@Table("users")
@Value
@RequiredArgsConstructor
public class User {

    @PrimaryKey
    private final String id;

    @Indexed
    private final String username;

    private final String password;

    private final Set<UserRole> roles;

    public static User createNew(final String username, final String password) {
        final String id = UUID.randomUUID().toString();
        return new User(id, username, password, UserRole.forNormalUser());
    }
}
