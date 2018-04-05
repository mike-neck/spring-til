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

import com.sun.tools.javac.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.function.Function;

@Configuration
public class DummyBatchConfiguration {

  private static final Logger logger = LoggerFactory.getLogger("dummy-batch");

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final IdGenerator idGenerator;

  public DummyBatchConfiguration(
      final JobBuilderFactory jobBuilderFactory,
      final StepBuilderFactory stepBuilderFactory,
      final IdGenerator idGenerator) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
    this.idGenerator = idGenerator;
  }

  public static class Item {
    final int index;
    final String item;

    Item(final int index, final String item) {
      this.index = index;
      this.item = item;
    }

    @Override
    public String toString() {
      //noinspection StringBufferReplaceableByString
      final StringBuilder sb = new StringBuilder("Item{");
      sb.append("index=").append(index);
      sb.append(", item='").append(item).append('\'');
      sb.append('}');
      return sb.toString();
    }
  }

  @Bean
  Step dummyStep() {
    final int[] count = {0};
    return stepBuilderFactory
        .get("dummyStep1")
        .<String, Item>chunk(3)
        .reader(new ListItemReader<>(List.of("foo", "bar", "baz", "qux")))
        .<String, Item>processor((Function<String, Item>) str -> new Item(count[0]++, str))
        .writer(items -> items.forEach(item -> logger.info("item: {}", item)))
        .build();
  }

  @Order(200)
  @Bean
  Job dummyJob() {
    return jobBuilderFactory.get("dummyJob").flow(dummyStep()).end().build();
  }
}
