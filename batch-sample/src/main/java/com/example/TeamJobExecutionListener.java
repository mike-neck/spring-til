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
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class TeamJobExecutionListener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger("teamJob"); 

    @Override
    public void beforeJob(final JobExecution jobExecution) {
        final Long jobId = jobExecution.getJobId();
        final Instant instant = jobExecution.getCreateTime().toInstant();
        final OffsetDateTime start = instant.atOffset(ZoneOffset.ofHours(9));
        logger.info("batch started: id: {}, start: {}", jobId, start);
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        final Long jobId = jobExecution.getJobId();
        final ExitStatus status = jobExecution.getExitStatus();
        final Instant instant = jobExecution.getEndTime().toInstant();
        final OffsetDateTime end = instant.atOffset(ZoneOffset.ofHours(9));
        logger.info("batch ended: id: {}, end: {}, status: {}", jobId, end, status);
    }
}
