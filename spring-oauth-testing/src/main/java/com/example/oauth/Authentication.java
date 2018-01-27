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

import com.example.ResourceConfig;
import com.example.jpa.AccessTokenEntity;
import com.example.jpa.ClientApplicationEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.Collections;

public class Authentication {

    private final AccessTokenEntity accessTokenEntity;
    private final AccessToken accessToken;
    private final ResourceUserAuthentication resourceUserAuthentication;

    public Authentication(final AccessTokenEntity accessTokenEntity) {
        this.accessTokenEntity = accessTokenEntity;
        accessToken = new AccessToken(accessTokenEntity);
        resourceUserAuthentication = new ResourceUserAuthentication(new OperatingUser(accessTokenEntity.getUser()));
    }

    public boolean isNotExpired() {
        return !accessToken.isExpired();
    }

    private OAuth2Request request() {
        final ClientApplicationEntity clientApplication = accessTokenEntity.getClientApplication();
        return new OAuth2Request(Collections.emptyMap(), Long.toHexString(clientApplication.getId()),
                resourceUserAuthentication.getAuthorities(), true, accessToken.getScope(), Collections.singleton(ResourceConfig.RESOURCE_ID),
                clientApplication.getRedirectUri(), Collections.emptySet(), Collections.emptyMap());
    }

    public OAuth2Authentication oauth2() {
        return new OAuth2Authentication(request(), resourceUserAuthentication);
    }
}
