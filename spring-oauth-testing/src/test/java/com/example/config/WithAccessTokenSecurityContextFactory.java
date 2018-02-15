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
package com.example.config;

import com.example.jpa.*;
import com.example.oauth.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class WithAccessTokenSecurityContextFactory implements WithSecurityContextFactory<WithAccessToken> {

    @Override
    public SecurityContext createSecurityContext(final WithAccessToken annotation) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        final GivenAccessToken accessToken = new GivenAccessToken(annotation);
        final OAuth2Authentication oauth2 = accessToken.oauth2();
        context.setAuthentication(oauth2);
        return context;
    }

    private static class GivenAccessToken {

        private final String accessToken;
        private final String refreshToken;
        private final Set<Scope> scopes;
        private final Date expiration;

        private final String signature;
        private final String username;
        private final Set<Authority> authorities;
        private final String applicationName;
        private final String redirectUrl;

        GivenAccessToken(final WithAccessToken accessToken) {
            this.accessToken = accessToken.accessToken();
            this.refreshToken = accessToken.refreshToken();
            this.scopes = Arrays.stream(accessToken.scope()).collect(Collectors.toSet());
            this.expiration = Date.from(offsetDateTime(accessToken.expiration()).toInstant());
            this.signature = accessToken.signature();
            this.username = accessToken.username();
            this.authorities = Arrays.stream(accessToken.authorities()).collect(Collectors.toSet());
            this.applicationName = accessToken.applicationName();
            this.redirectUrl = accessToken.redirectUrl();
        }

        private static OffsetDateTime offsetDateTime(final ExpirationDate expiration) {
            return OffsetDateTime.of(localDateTime(expiration), ZoneOffset.ofHours(expiration.offset()));
        }

        private static LocalDateTime localDateTime(final ExpirationDate expiration) {
            return LocalDateTime.of(expiration.year(), expiration.month(), expiration.day(), expiration.hour(), expiration.minutes(), expiration.seconds());
        }

        AccessTokenEntity accessTokenEntity() {
            final AccessTokenEntity accessTokenEntity = new AccessTokenEntity(user(), client(), accessToken, refreshToken, scopes);
            accessTokenEntity.setExpiration(expiration);
            return accessTokenEntity;
        }

        private ClientApplicationEntity client() {
            return new ClientApplicationEntity(applicationName, redirectUrl);
        }

        private UserEntity user() {
            return new UserEntity(signature, username, "", authorities);
        }

        private OAuth2Authentication oauth2() {
            final AccessTokenEntity accessTokenEntity = accessTokenEntity();
            final Authentication authentication = new Authentication(accessTokenEntity);
            return authentication.oauth2();
        }
    }
}
