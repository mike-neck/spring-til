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

import com.example.exception.AccessTokenExpiredException;
import com.example.jpa.AccessTokenEntity;
import com.example.oauth.AccessToken;
import com.example.oauth.Authentication;
import com.example.repository.AccessTokenRepository;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

@Service("tokenServices")
public class TokenServices implements ResourceServerTokenServices {

    private final AccessTokenRepository accessTokenRepository;

    public TokenServices(final AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public OAuth2Authentication loadAuthentication(final String accessToken) throws AuthenticationException, InvalidTokenException {
        final Optional<AccessTokenEntity> token = accessTokenRepository.findByAccessToken(accessToken);
        return token.map(Authentication::new)
                .map(Optional::of)
                .orElseThrow(invalidAccessToken(accessToken))
                .filter(Authentication::isNotExpired)
                .map(Authentication::oauth2)
                .orElseThrow(tokenIsExpired(accessToken));
    }

    private Supplier<ClientAuthenticationException> tokenIsExpired(final String accessToken) {
        return () -> new AccessTokenExpiredException("expired access_token[" + accessToken + "]");
    }

    @Override
    public OAuth2AccessToken readAccessToken(final String accessToken) {
        final Optional<AccessTokenEntity> token = accessTokenRepository.findByAccessToken(accessToken);
        return token.map(AccessToken::new).orElseThrow(invalidAccessToken(accessToken));
    }

    private Supplier<InvalidTokenException> invalidAccessToken(final String accessToken) {
        return () -> new InvalidTokenException("invalid access_token[" + accessToken + "]");
    }
}
