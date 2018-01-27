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

import com.example.jpa.*;
import com.example.repository.AccessTokenRepository;
import com.example.repository.ClientApplicationRepository;
import com.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EnableResourceServer
@SpringBootApplication
public class Application {

    private final UserRepository userRepository;
    private final ClientApplicationRepository clientApplicationRepository;
    private final AccessTokenRepository accessTokenRepository;

    public Application(final UserRepository userRepository, final ClientApplicationRepository clientApplicationRepository,
            final AccessTokenRepository accessTokenRepository) {
        this.userRepository = userRepository;
        this.clientApplicationRepository = clientApplicationRepository;
        this.accessTokenRepository = accessTokenRepository;
    }

    private static <T> Set<T> set(final T... ts) {
        return Arrays.stream(ts).collect(Collectors.toSet());
    }

    private static OffsetDateTime offsetDateTime(final int year, final Month month, final  int day, final int hour, final int minutes, final int second) {
        final LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minutes, second);
        return offsetDateTime(localDateTime);
    }

    private static OffsetDateTime offsetDateTime(final LocalDateTime localDateTime) {
        final ZoneId zoneId = ZoneId.systemDefault();
        final ZoneOffset offset = zoneId.getRules().getOffset(localDateTime);
        return OffsetDateTime.of(localDateTime, offset);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            final List<UserEntity> us = userRepository.save(Arrays.asList(
                    new UserEntity("user-01", "ユーザー01", "password-01", set(Authority.USER, Authority.ADMIN, Authority.DEVELOPER)),
                    new UserEntity("user-02", "ユーザー02", "password-02", set(Authority.USER)),
                    new UserEntity("user-03", "ユーザー03", "password-03", set(Authority.USER, Authority.DEVELOPER))
            ));
            final List<ClientApplicationEntity> cs = clientApplicationRepository.save(Arrays.asList(
                    new ClientApplicationEntity("default app", "https://example.com/default-app"),
                    new ClientApplicationEntity("custom app", "https://example.com/custom-app")
            ));
            final List<AccessTokenEntity> as = accessTokenRepository.save(Arrays.asList(
                    new AccessTokenEntity(us.get(0), cs.get(0), "U01C01", "RU01RC01", set(Scope.USER, Scope.MESSAGE, Scope.MANAGEMENT), offsetDateTime(LocalDateTime.now().minusDays(1L))),
                    new AccessTokenEntity(us.get(1), cs.get(0), "U02C01" ,"RU02RC01", set(Scope.USER), offsetDateTime(LocalDateTime.now().minusDays(1L).minusHours(20L))),
                    new AccessTokenEntity(us.get(2), cs.get(0), "U03C01", "RU03RC01", set(Scope.USER, Scope.MESSAGE), offsetDateTime(LocalDateTime.now().minusDays(3L))),
                    new AccessTokenEntity(us.get(0), cs.get(1), "U01C02", "RU01RC02", set(Scope.USER, Scope.MANAGEMENT), offsetDateTime(LocalDateTime.now()))
            ));
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
