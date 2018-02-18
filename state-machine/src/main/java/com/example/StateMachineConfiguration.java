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

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.factory.Sets;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.Optional;

@Configuration
@EnableStateMachine
@Slf4j
public class StateMachineConfiguration extends EnumStateMachineConfigurerAdapter<IssueStates, UserAction> {

    @Override
    public void configure(final StateMachineConfigurationConfigurer<IssueStates, UserAction> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true)
                .listener(new StateMachineListenerAdapter<IssueStates, UserAction>(){
                    @Override
                    public void stateChanged(final State<IssueStates, UserAction> from, final State<IssueStates, UserAction> to) {
                        final Optional<IssueStates> beforeState = Optional.ofNullable(from).map(State::getId);
                        final Optional<IssueStates> afterState = Optional.ofNullable(to).map(State::getId);
                        log.info("state change from: {}, to: {}", beforeState, afterState);
                    }
                });
    }

    @Override
    public void configure(final StateMachineStateConfigurer<IssueStates, UserAction> states) throws Exception {
        states.withStates().initial(IssueStates.NO_PROBLEM).states(Sets.mutable.of(IssueStates.values()).asUnmodifiable());
    }

    @Override
    public void configure(final StateMachineTransitionConfigurer<IssueStates, UserAction> transitions) throws Exception {
        transitions
                .withExternal()
                .source(IssueStates.NO_PROBLEM).event(UserAction.FIND_ISSUE).target(IssueStates.CREATED)
                .and()
                .withExternal()
                .source(IssueStates.CREATED).event(UserAction.START_WORKING).target(IssueStates.IN_PROCESS)
                .and()
                .withExternal()
                .source(IssueStates.IN_PROCESS).event(UserAction.DONE_WORKING).target(IssueStates.FIXED)
                .and()
                .withExternal()
                .source(IssueStates.FIXED).event(UserAction.TEST_SUCCEEDED).target(IssueStates.VERIFIED)
                .and()
                .withExternal()
                .source(IssueStates.FIXED).event(UserAction.TEST_FAILED).target(IssueStates.RE_OPEN)
                .and()
                .withExternal()
                .source(IssueStates.RE_OPEN).event(UserAction.START_WORKING).target(IssueStates.IN_PROCESS);
    }
}
