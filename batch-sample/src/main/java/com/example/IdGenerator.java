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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class IdGenerator {

  private final LocalDateTime from = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0);

  private final ZoneId utc = ZoneId.of("UTC");

  private final AtomicInteger count = new AtomicInteger(0);

  public long generateId() {
    final LocalDateTime now = LocalDateTime.now(utc);
    final Duration duration = Duration.between(from, now);
    final int high = (int) duration.getSeconds();
    final int low = count.incrementAndGet();
    final ByteBuffer buffer = ByteBuffer.allocate(8);
    buffer.putInt(high);
    buffer.putInt(low);
    buffer.flip();
    return buffer.getLong();
  }
}
