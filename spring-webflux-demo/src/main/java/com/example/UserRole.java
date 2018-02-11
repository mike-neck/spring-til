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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum UserRole {

    VIEW_SELF_PROFILE,
    EDIT_SELF_PROFILE,
    VIEW_SELF_MESSAGE,
    EDIT_SELF_MESSAGE,
    VIEW_OTHER_PROFILE,
    EDIT_OTHER_PROFILE,
    VIEW_OTHER_MESSAGE,
    EDIT_OTHER_MESSAGE;

    public static Set<UserRole> forNormalUser() {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                VIEW_SELF_PROFILE,
                EDIT_SELF_PROFILE,
                VIEW_SELF_MESSAGE,
                EDIT_SELF_MESSAGE,
                VIEW_OTHER_PROFILE,
                VIEW_OTHER_MESSAGE
        )));
    }

    public static Set<UserRole> forLimitedUser() {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                VIEW_SELF_PROFILE,
                VIEW_SELF_MESSAGE,
                VIEW_OTHER_PROFILE,
                VIEW_OTHER_MESSAGE
        )));
    }

    public static Set<UserRole> forAdministrativeUser() {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                VIEW_SELF_PROFILE,
                EDIT_SELF_PROFILE,
                VIEW_SELF_MESSAGE,
                EDIT_SELF_MESSAGE,
                VIEW_OTHER_PROFILE,
                EDIT_OTHER_PROFILE,
                VIEW_OTHER_MESSAGE,
                EDIT_OTHER_MESSAGE
        )));
    }
}
