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

import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@SpringBootApplication
public class App implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger("app");

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  private final JobLauncher jobLauncher;
  private final List<Job> jobs;

  public App(final JobLauncher jobLauncher, final List<Job> jobs) {
    this.jobLauncher = jobLauncher;
    this.jobs = jobs;
  }

  @Override
  public void run(final String... args) throws Exception {
    Lists.adapt(jobs)
        .collectWithIndex((job, index) -> new JobExec(index, job))
        .each(
            jobExec -> {
              try {
                logger.info("[{}]job: {}", jobExec.index, jobExec.job.getName());
                jobLauncher.run(jobExec.job, jobExec.params());
              } catch (JobExecutionAlreadyRunningException
                  | JobRestartException
                  | JobInstanceAlreadyCompleteException
                  | JobParametersInvalidException e) {
                logger.warn("exception in job execution.", e);
              }
            });
  }
}

class JobExec {
  final int index;
  final Job job;

  JobExec(final int index, final Job job) {
    this.index = index;
    this.job = job;
  }

  JobParameters params() {
    return new JobParameters(
        Maps.immutable.of("job-exec-number", new JobParameter((long) index)).toMap());
  }
}
