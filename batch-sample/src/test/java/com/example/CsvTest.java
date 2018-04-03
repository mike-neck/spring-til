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

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;

class CsvTest {

  @Test
  void readCsv() throws IOException {
    final CsvMapper mapper = new CsvMapper();
    final CsvSchema schema = CsvSchema.emptySchema().withHeader();
    final ObjectReader reader = mapper.readerFor(User.Line.class).with(schema);
    final Iterable<User.Line> userLines =
        () -> {
          try {
            return reader.readValues("username\n\"foo\"\n\"bar\"");
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }
        };
    final ImmutableList<User.Line> users = Lists.immutable.ofAll(userLines);
    System.out.println(users);
  }
}
