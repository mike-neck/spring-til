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
package com.example.oauth;

import com.example.jpa.AccessTokenEntity;
import com.example.jpa.Scope;
import lombok.ToString;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
public class AccessToken implements OAuth2AccessToken {

    private final String accessToken;
    private final String refreshToken;
    private final Set<Scope> scopes;
    private final OffsetDateTime expiration;

    public AccessToken(final AccessTokenEntity accessToken) {
        this(accessToken.getAccessToken(), accessToken.getRefreshToken(), accessToken.getScopes(),
                OffsetDateTime.from(accessToken.getExpiration().toInstant().atZone(ZoneId.systemDefault())));
    }

    private AccessToken(final String accessToken, final String refreshToken, final Set<Scope> scopes, final OffsetDateTime expiration) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.scopes = scopes;
        this.expiration = expiration;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return Collections.emptyMap();
    }

    @Override
    public Set<String> getScope() {
        return scopes.stream().map(Scope::asString).collect(Collectors.toSet());
    }

    @Override
    public OAuth2RefreshToken getRefreshToken() {
        return () -> refreshToken;
    }

    @Override
    public String getTokenType() {
        return OAuth2AccessToken.BEARER_TYPE;
    }

    @Override
    public boolean isExpired() {
        final OffsetDateTime now = OffsetDateTime.now();
        return expiration.isBefore(now);
    }

    @Override
    public Date getExpiration() {
        return Date.from(expiration.toInstant());
    }

    @Override
    public int getExpiresIn() {
        final OffsetDateTime now = OffsetDateTime.now();
        final Duration expiredIn = Duration.between(now, expiration);
        return (int) expiredIn.get(ChronoUnit.SECONDS);
    }

    @Override
    public String getValue() {
        return accessToken;
    }
}
