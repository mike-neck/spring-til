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
package com.example.jpa;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum Scope {
    USER("user"),
    MESSAGE("message"),
    MANAGEMENT("management");

    private final String text;

    Scope(final String text) {
        this.text = text;
    }

    public String asString() {
        return text;
    }

    public static Set<Scope> all() {
        return Arrays.stream(values()).collect(Collectors.toSet());
    }

    public static Scope fromString(final String text) {
        return Arrays.stream(values())
                .filter(s -> s.text.equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("scope [" + text + "] is invalid."));
    }
}
