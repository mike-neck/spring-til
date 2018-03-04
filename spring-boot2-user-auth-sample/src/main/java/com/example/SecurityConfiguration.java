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

import com.example.user.UserAccountRepository;
import com.example.user.UserAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfiguration {

  private final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

  private final UserAccountRepository userAccountRepository;

  public SecurityConfiguration(final UserAccountRepository userAccountRepository) {
    this.userAccountRepository = userAccountRepository;
  }

  @Bean
  ReactiveUserDetailsService userDetailsService() {
    logger.info("configure user authentication.");
    return username -> {
      logger.info("in-coming user: {}", username);
      return userAccountRepository.findByUserLoginId(username).map(UserAdapter::new);
    };
  }

  @Bean
  SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {
    return http.httpBasic()
        .and()
        .authorizeExchange()
        .pathMatchers(HttpMethod.GET, "/talks/**")
        .hasAuthority("USER")
        .pathMatchers(HttpMethod.GET, "/test/**")
        .hasAuthority("ADMIN")
        .and()
        .build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }
}
