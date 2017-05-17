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

import com.example.logger
import com.example.message.client.ErrorMessage
import com.example.message.client.NewMessage
import com.example.message.server.ErrorMsg
import com.example.message.server.Message
import com.example.message.server.NewMsg
import org.slf4j.Logger
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Controller
class MessageController {

    @MessageMapping("/message")
    @SendTo("/contents/message")
    fun message(newMessage: NewMessage): Message<NewMsg> = TimeUnit.SECONDS.sleep(2L)
            .apply { logger.debug(newMessage.toString()) }
            .let { NewMsg(LocalDateTime.now(), newMessage.text) }
            .let { Message.newMessage(it) }

    @MessageMapping("/error-message")
    @SendTo("/contents/message")
    fun errorMessage(error: ErrorMessage): Message<ErrorMsg> = TimeUnit.SECONDS.sleep(2L)
            .apply { logger.debug(error.toString()) }
            .let { ErrorMsg(LocalDateTime.now(), error.errorMessage) }
            .let { Message.errorMessage(it) }

    companion object {
        val logger: Logger = logger<MessageController>()
    }
}
