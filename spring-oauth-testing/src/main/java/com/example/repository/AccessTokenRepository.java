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

import com.example.jpa.AccessTokenEntity;
import com.example.jpa.AccessTokenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessTokenEntity, AccessTokenId> {

    @Query("select at from AccessTokenEntity at join fetch at.scopes join fetch at.clientApplication join fetch at.user as u join fetch u.authorities where at.accessToken = :accessToken")
    Optional<AccessTokenEntity> findByAccessToken(@Param("accessToken") final String accessToken);
}
