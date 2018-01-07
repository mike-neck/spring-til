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

import static java.util.stream.Collectors.toSet;

public enum  Authority {

    READ_MESSAGE(true),
    CREATE_MESSAGE(true),
    DELETE_MESSAGE(true),
    READ_SELF_PROFILE(true),
    UPDATE_SELF_PROFILE(true),
    DELETE_SELF_PROFILE(true),
    READ_OTHER_PROFILE(false),
    UPDATE_OTHER_PROFILE(false),
    DELETE_OTHER_PROFILE(false)
    ;

    private final boolean defaultAuthority;

    Authority(final boolean defaultAuthority) {
        this.defaultAuthority = defaultAuthority;
    }

    private boolean isDefaultAuthority() {
        return defaultAuthority;
    }

    public static Set<Authority> defaultSet() {
        return Arrays.stream(values())
                .filter(Authority::isDefaultAuthority)
                .collect(toSet());
    }

    public static void main(String[] args) {
        Arrays.stream(values())
                .filter(Authority::isDefaultAuthority)
                .map(Enum::name)
                .map(s -> String.format("  (1, '%s'),", s))
                .forEach(System.out::println);
    }
}
