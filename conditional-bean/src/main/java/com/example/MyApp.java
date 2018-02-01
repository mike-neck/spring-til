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

import com.example.config.MyProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class MyApp {

    

    interface Runner {
        Optional<String> run();
    }

    @Configuration
    @EnableConfigurationProperties({MyProperties.class})
    @ConditionalOnProperty(prefix = "my", name = "active", havingValue = "true")
    public static class ActiveRunnerConfiguration {

        @Bean
        Runner activeRunner(final MyProperties myProperties) {
            return myProperties::fileName;
        }
    }

    @Configuration
    @ConditionalOnProperty(value = { "app" }, havingValue = "empty")
    public static class EmptyRunnerConfiguration {

        @Bean
        Runner emptyRunner() {
            return new EmptyRunner();
        }
    }

    public static class EmptyRunner implements Runner {
        @Override
        public Optional<String> run() {
            return Optional.empty();
        }
    }

    @Configuration
    @ConditionalOnProperty(value = { "foo.bar" })
    public static class FooBarConfiguration {

        @Bean
        Runner fooBarRunner(@Value("${foo.bar}") final String fooBar) {
            return () -> Optional.ofNullable(fooBar);
        }
    }
}
