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

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class MyAppTest {

    @RunWith(SpringRunner.class)
    @TestPropertySource(properties = { "my.active=true", "my.nest.file-name=test" })
    @ContextConfiguration(classes = { MyApp.class })
    public static class ActiveRunnerTest {

        @Autowired
        MyApp.Runner runner;

        @Test
        public void test() {
            assertThat(runner.run()).contains("test");
        }
    }

    @RunWith(SpringRunner.class)
    @TestPropertySource(properties = { "app=empty" })
    @ContextConfiguration(classes = { MyApp.class })
    public static class EmptyRunnerTest {

        @Autowired
        MyApp.Runner runner;

        @Test
        public void test() {
            assertThat(runner).isInstanceOf(MyApp.EmptyRunner.class);
            assertThat(runner.run()).isEmpty();
        }
    }

    @RunWith(SpringRunner.class)
    @TestPropertySource(properties = { "foo.bar=baz" })
    @ContextConfiguration(classes = { MyApp.class })
    public static class FooBarRunnerTest {

        @Autowired
        MyApp.Runner runner;

        @Test
        public void test() {
            assertThat(runner.run()).contains("baz");
        }
    }
}
