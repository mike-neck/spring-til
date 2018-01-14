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

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

    private static final String CLIENT_ENDPOINT = "http://localhost:5000/db-app/application-info";

    private final RestTemplate restTemplate;

    public ClientDetailsServiceImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ClientDetails loadClientByClientId(final String clientId) throws ClientRegistrationException {
        final ResponseEntity<ClientEntity> response = restTemplate.getForEntity(String.format("%s?client_id={client_id}", CLIENT_ENDPOINT), ClientEntity.class,
                new HashMap<String, String>() {{
                    put("client_id", clientId);
                }});
        final ClientEntity client = response.getBody();
        return new ClientDetailsImpl(client.toImmutable());
    }
}
