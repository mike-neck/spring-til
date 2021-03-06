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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ MyProperties.class })
public class MyConfig {

    @Configuration
    @ConditionalOnProperty(value = { "my.active" }, matchIfMissing = true)
    public static class Missing {
        @Bean
        MyFileSupplier emptySupplier() {
            return new EmptySupplier();
        }
    }

    @Configuration
    @ConditionalOnProperty(value = { "my.active" }, havingValue = "false")
    public static class Inactive {

        @Bean
        MyFileSupplier inactiveSupplier() {
            return new InactiveSupplier();
        }
    }

    @Configuration
    @ConditionalOnProperty(value = { "my.nest.file-name" })
    public static class Active {

        @Bean
        MyFileSupplier activeSupplier(final MyProperties myProperties) {
            return myProperties::fileName;
        }
    }
}
