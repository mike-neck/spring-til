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
package com.example.resources.user;

import com.example.oauth.OperatingUser;
import com.example.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
public class UserResource {

    private final UserRepository userRepository;

    public UserResource(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    List<User> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(User::new)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "me")
    ResponseEntity<?> getMe(@AuthenticationPrincipal final OperatingUser operatingUser) {
        return userRepository.findById(operatingUser.getUserId())
                .map(User::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
