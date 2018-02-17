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

import com.example.config.ExpirationDate;
import com.example.config.WithAccessToken;
import com.example.jpa.Authority;
import com.example.jpa.Scope;
import com.example.jpa.UserEntity;
import com.example.repository.AccessTokenRepository;
import com.example.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Month;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { TokenServicesTest.Config.class, TokenServices.class, ResourceConfig.class })
@ComponentScan(basePackages = { "com.example.resources" })
@WebMvcTest
@WebAppConfiguration
public class TokenServicesTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    UserRepository userRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void アクセストークン無しでusersリソースへアクセスすると401エラー() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithAccessToken(username = "user-name", signature = "test-user", authorities = {Authority.USER}, scope = {Scope.USER}, accessToken = "accessToken",
            expiration = @ExpirationDate(year = 2030, month = Month.DECEMBER, day = 31, offset = 9))
    public void アクセストークンがあり適切なスコープを与えられているアプリケーション() throws Exception {
        when(userRepository.findAll()).thenReturn(Arrays.asList(
                UserEntity.of(100L, "test1", "test-user", "aabbcc", new Date(), new Date(), Authority.USER, Authority.DEVELOPER),
                UserEntity.of(200L, "test2", "test-user2", "wweerr", new Date(), new Date(), Authority.USER)
        ));
        mockMvc.perform(get("/users").header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].user_id").value("test1"))
                .andExpect(jsonPath("$[0].username").value("test-user"));
    }

    @EnableResourceServer
    @Configuration
    static class Config {

        @Bean
        AccessTokenRepository accessTokenRepository() {
            return mock(AccessTokenRepository.class);
        }

        @Bean
        UserRepository userRepository() {
            return mock(UserRepository.class);
        }
    }
}
