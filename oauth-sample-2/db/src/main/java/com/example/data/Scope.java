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
package com.example.data;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;

public enum Scope {

    READ_MESSAGE("read:message"),
    WRITE_MESSAGE("write:message"),
    READ_PROFILE("read:profile"),
    WRITE_PROFILE("write:profile");

    private final String value;

    Scope(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Scope fromString(final String value) {
        final Predicate<Scope> nameMatches = s -> s.value.equals(value);
        return Arrays.stream(values()).filter(nameMatches)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static Set<String> allAsString() {
        return Arrays.stream(values())
                .map(Scope::getValue)
                .collect(toSet());
    }
}
