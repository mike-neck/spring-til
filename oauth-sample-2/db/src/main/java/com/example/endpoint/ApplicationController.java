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
package com.example.endpoint;

import com.example.data.Scope;
import com.example.entity.ApplicationEntity;
import com.example.repository.ApplicationRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "application-info")
public class ApplicationController {

    private final ApplicationRepository applicationRepository;

    public ApplicationController(final ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ApplicationEntity getApplication(@RequestParam(value = "client_id") final String clientId) {
        return applicationRepository.findApplication(clientId)
                .map(a -> new ApplicationEntity(a.getClientId(),
                        a.getClientSecret(),
                        Scope.allAsString(),
                        a.getUser().getAuthoritiesAsString(),
                        a.getRedirectUri()))
                .orElseThrow(ResourceNotFoundException::new);
    }
}
