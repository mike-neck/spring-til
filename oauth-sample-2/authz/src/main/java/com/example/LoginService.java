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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService implements UserDetailsService {

    private static final String USER_ENDPOINT = "http://localhost:5000/db-app/user-internal";

    private final RestTemplate restTemplate;

    @Autowired
    public LoginService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Map<String, String> query = new HashMap<String, String>(){{ put("username", username); }};
        try {
            final ResponseEntity<UserEntity> response = restTemplate.getForEntity(String.format("%s?username={username}", USER_ENDPOINT), UserEntity.class, query);
            final UserEntity user = response.getBody();
            return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
        } catch (RestClientException e) {
            throw new UsernameNotFoundException("user [" + username + "] not found.");
        }
    }
}
