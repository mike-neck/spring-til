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

import com.example.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebFluxSecurity
public class Config {

    private final UserRepository userRepository;

    public Config(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    ReactiveUserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .log("authentication")
                .map(AuthenticatedUser::new);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // ロールの設定をすると 401 UnAuthorized になってしまう…
//    @Bean
//    SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {
//        return http.authorizeExchange()
//                .pathMatchers(HttpMethod.GET, "/users/**").hasAuthority(UserRole.VIEW_OTHER_PROFILE.name())
//                .pathMatchers(HttpMethod.POST, "/users/**").hasAuthority(UserRole.EDIT_OTHER_PROFILE.name())
//                .pathMatchers(HttpMethod.PUT, "/users/**").hasAuthority(UserRole.EDIT_OTHER_PROFILE.name())
//                .pathMatchers(HttpMethod.DELETE, "/users/**").hasAuthority(UserRole.EDIT_OTHER_PROFILE.name())
//                .pathMatchers(HttpMethod.GET, "/me/**").hasAuthority(UserRole.VIEW_SELF_PROFILE.name())
//                .pathMatchers(HttpMethod.POST, "/me/**").hasAuthority(UserRole.EDIT_SELF_PROFILE.name())
//                .pathMatchers(HttpMethod.DELETE, "/me/**").hasAuthority(UserRole.EDIT_SELF_PROFILE.name())
//            .and()
//                .build();
//    }
}
