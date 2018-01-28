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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ResourceConfig extends ResourceServerConfigurerAdapter {

    public static final String RESOURCE_ID = "oauth2-resource-test";

    private final ResourceServerTokenServices tokenServices;

    public ResourceConfig(final ResourceServerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenServices)
                .resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/users/**").access("#oauth2.hasScope(T(com.example.jpa.Scope).USER.asString())")
                .antMatchers("/messages/**").access("#oauth2.hasScope(T(com.example.jpa.Scope).MESSAGE.asString())")
                .antMatchers("/", "/lib/*", "/images/*", "/css/*", "/swagger-ui.js", "/swagger-ui.min.js", "/api-docs", "/fonts/*", "/api-docs/*",
                        "/api-docs/default/*", "/o2c.html", "index.html", "/webjars/**", "/swagger-ui.html").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .modules(new JavaTimeModule().addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }
}
