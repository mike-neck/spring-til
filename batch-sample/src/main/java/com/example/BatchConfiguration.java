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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.FileSystemResourceLoader;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  private static final Logger logger = LoggerFactory.getLogger("teamJob");

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final IdGenerator idGenerator;

  @SafeVarargs
  private static <A> A[] array(final A... values) {
    return values;
  }

  public BatchConfiguration(
      final JobBuilderFactory jobBuilderFactory,
      final StepBuilderFactory stepBuilderFactory,
      final IdGenerator idGenerator) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
    this.idGenerator = idGenerator;
  }

  @Bean
  FlatFileItemReader<Team.Line> reader() {
    final FileSystemResourceLoader fileSystemResourceLoader = new FileSystemResourceLoader();
    return new FlatFileItemReaderBuilder<Team.Line>()
        .name("teamLineReader")
        .resource(fileSystemResourceLoader.getResource("test-data/team.csv"))
        .delimited()
        .delimiter(",")
        .names(array("identifier", "name"))
        .linesToSkip(1)
        .fieldSetMapper(
            new BeanWrapperFieldSetMapper<Team.Line>() {
              {
                setTargetType(Team.Line.class);
              }
            })
        .build();
  }

  @Bean
  TeamItemProcessor teamItemProcessor() {
    return new TeamItemProcessor(idGenerator);
  }

  @Bean
  ItemWriter<Team> teamItemWriter() {
    return items -> items.forEach(item -> logger.info("write team: {}", item));
  }

  @Bean
  Step teamStep() {
    return stepBuilderFactory
        .get("teamStep")
        .<Team.Line, Team>chunk(2)
        .reader(reader())
        .processor(teamItemProcessor())
        .writer(teamItemWriter())
        .build();
  }

  @Order(400)
  @Bean
  Job teamJob(final Step teamStep, final TeamJobExecutionListener jobExecutionListener) {
    return jobBuilderFactory
        .get("teamJob")
        .incrementer(new RunIdIncrementer())
        .listener(jobExecutionListener)
        .flow(teamStep)
        .end()
        .build();
  }
}
