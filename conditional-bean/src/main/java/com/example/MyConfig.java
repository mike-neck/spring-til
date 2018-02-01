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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties({ MyProperties.class })
public class MyConfig {

    @Configuration
    @ConditionalOnProperty(prefix = "my", name = "active", matchIfMissing = true)
    public static class Missing {
        @Bean
        MyActiveSupplier missingBooleanSupplier() {
            return () -> Optional.of(Boolean.FALSE);
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = "my", name = "active", havingValue = "true")
    @EnableConfigurationProperties({ MyProperties.class })
    public static class Active {
        @Bean
        MyActiveSupplier activeBooleanSupplier(final MyProperties myProperties) {
            return () -> Optional.ofNullable(myProperties).map(MyProperties::isActive);
        }
    }

    public interface MyActiveSupplier {
        Optional<Boolean> get();
    }
}
