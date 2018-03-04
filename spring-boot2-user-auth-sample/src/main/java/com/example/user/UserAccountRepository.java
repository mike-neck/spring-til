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

import com.example.IdGenerator;
import com.example.model.UserAccount;
import org.eclipse.collections.api.map.MutableMap;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class UserAccountRepository {

    private final IdGenerator idGenerator;

    private final MutableMap<String , UserAccount> users;

    public UserAccountRepository(final IdGenerator idGenerator,
            final MutableMap<String, UserAccount> userAccounts) {
        this.idGenerator = idGenerator;
        this.users = userAccounts;
    }

    public Mono<UserAccount> findByUserLoginId(final String loginId) {
        return Mono.justOrEmpty(Optional.of(users.get(loginId)));
    }
}
