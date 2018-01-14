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
package com.example.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ClientDetailsImpl implements ClientDetails {

    private final ClientEntity.Immutable client;

    public ClientDetailsImpl(final ClientEntity.Immutable client) {
        this.client = client;
    }

    @Override
    public String getClientId() {
        return client.getClientId();
    }

    @Override
    public Set<String> getResourceIds() {
        return Collections.emptySet();
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return client.getClientSecret();
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        return client.getScopes();
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return Collections.singleton("code");
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return Collections.singleton(client.getRedirectUri());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return client.authorityAsFrameworkType();
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return 2 * 60 * 60;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return 30 * 24 * 60 * 60;
    }

    @Override
    public boolean isAutoApprove(final String scope) {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return Collections.emptyMap();
    }
}
