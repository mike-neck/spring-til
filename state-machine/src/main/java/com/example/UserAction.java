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

import java.util.Optional;

public enum UserAction {

    FIND_ISSUE,
    START_WORKING,
    DONE_WORKING,
    TEST_SUCCEEDED,
    TEST_FAILED;

    public static Optional<UserAction> find(final String action) {
        try {
            final UserAction userAction = valueOf(action);
            return Optional.of(userAction);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
