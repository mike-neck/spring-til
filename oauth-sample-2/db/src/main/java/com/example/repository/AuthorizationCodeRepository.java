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
package com.example.repository;

import com.example.data.AuthorizationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource(path = "auth_code")
public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode, Long> {

    @RestResource(path = "by_code_and_user_id")
    @Query("select ac from AuthorizationCode as ac where ac.authorizationCode = :code and ac.user.id = :user_id")
    Optional<AuthorizationCode> findByAuthorizationCodeAndUserId(@Param("code") final String authorizationCode, @Param("user_id") final Long userId);

    @RestResource(path = "by_code")
    Optional<AuthorizationCode> findByAuthorizationCode(@Param("code") final String authorizationCode);
}
