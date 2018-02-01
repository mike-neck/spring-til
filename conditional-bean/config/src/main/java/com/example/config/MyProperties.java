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
package com.example.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Optional;

@ConfigurationProperties(prefix = "my")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyProperties {

    private boolean active = false;

    private NestProperties nest;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NestProperties {
        private String fileName;

        Optional<String> fileName() {
            return Optional.ofNullable(fileName);
        }
    }

    public Optional<String> fileName() {
        return Optional.ofNullable(nest)
                .flatMap(NestProperties::fileName);
    }
}
