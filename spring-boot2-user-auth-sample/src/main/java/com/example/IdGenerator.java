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

import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

@Component
public class IdGenerator {

    private static final LocalDateTime BASE = LocalDateTime.of(2018, Month.JANUARY, 1, 1, 1);

    private final Clock clock;

    public IdGenerator(final Clock clock) {
        this.clock = clock;
    }

    public long generateLong() {
        final LocalDateTime now = LocalDateTime.now(clock);
        final Duration duration = Duration.between(BASE, now);
        final int high = (int) duration.getSeconds();
        final int low = duration.getNano();
        final byte[] hbs = ByteBuffer.allocate(4).putInt(high).array();
        final byte[] lbs = ByteBuffer.allocate(4).putInt(low).array();
        final byte[] bytes = new byte[8];
        System.arraycopy(hbs, 0, bytes, 0, 4);
        System.arraycopy(lbs, 0, bytes, 4, 4);
        final ByteBuffer buffer = ByteBuffer.allocate(8).put(bytes, 0, 8);
        buffer.flip();
        return buffer.getLong();
    }
}
