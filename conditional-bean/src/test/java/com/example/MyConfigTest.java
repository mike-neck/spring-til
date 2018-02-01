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

import com.example.config.MyProperties;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class MyConfigTest {

    @RunWith(SpringRunner.class)
    @TestPropertySource(properties = { "my.active=true" })
    @ActiveProfiles({ "test" })
    @ContextConfiguration(classes = { MyConfig.class, MyProperties.class})
    public static class ActiveBooleanSupplier {

        @Autowired
        MyConfig.MyActiveSupplier booleanSupplier;

        @Test
        public void test() {
            assertThat(booleanSupplier.get()).contains(true);
        }
    }

    @Ignore
    @RunWith(SpringRunner.class)
    @TestPropertySource(properties = { "my.action=true" })
    @ActiveProfiles({ "test" })
    @ContextConfiguration(classes = { MyConfig.class, MyProperties.class})
    public static class TypoBooleanSupplier {

        @Autowired
        MyConfig.MyActiveSupplier booleanSupplier;

        @Test
        public void test() {
            assertThat(booleanSupplier.get()).contains(false);
        }
    }
}
