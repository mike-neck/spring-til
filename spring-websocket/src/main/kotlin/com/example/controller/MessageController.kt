/*
 * Copyright 2017 Shinya Mochida
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
package com.example.controller

import com.example.message.client.NewMessage
import com.example.message.server.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Controller
open class MessageController {

    @MessageMapping("/message")
    @SendTo("/contents/message")
    open fun message(newMessage: NewMessage): Message = TimeUnit.SECONDS.sleep(2L)
            .let { Message(LocalDateTime.now(), newMessage.text) }
}
