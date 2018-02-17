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
import com.example.repository.AccessTokenRepository;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

public class WithAccessTokenSecurityContextFactory implements WithSecurityContextFactory<WithAccessToken> {

    private final AccessTokenRepository accessTokenRepository;

    public WithAccessTokenSecurityContextFactory(final AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public SecurityContext createSecurityContext(final WithAccessToken annotation) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        final GivenAccessToken accessToken = new GivenAccessToken(annotation);
        final OAuth2Authentication oauth2 = accessToken.oauth2();

        final MockingDetails mockingDetails = Mockito.mockingDetails(accessTokenRepository);
        if (mockingDetails.isMock()) {
            when(accessTokenRepository.findByAccessToken(accessToken.accessToken))
                    .thenReturn(Optional.of(accessToken.accessTokenEntity()));
        }

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

        private final long userId;
        private final long applicationId;

        GivenAccessToken(final WithAccessToken accessToken) {
            this.userId = accessToken.userId();
            this.applicationId = accessToken.applicationId();
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
            accessTokenEntity.setId(new AccessTokenId(applicationId, userId));
            return accessTokenEntity;
        }

        private ClientApplicationEntity client() {
            final ClientApplicationEntity clientApplication = new ClientApplicationEntity(applicationName, redirectUrl);
            clientApplication.setId(applicationId);
            return clientApplication;
        }

        private UserEntity user() {
            final UserEntity userEntity = new UserEntity(signature, username, "", authorities);
            userEntity.setId(userId);
            return userEntity;
        }

        private OAuth2Authentication oauth2() {
            final AccessTokenEntity accessTokenEntity = accessTokenEntity();
            final Authentication authentication = new Authentication(accessTokenEntity);
            return authentication.oauth2();
        }
    }
}
