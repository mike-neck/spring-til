package com.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.StompEndpointRegistry

@SpringBootApplication
open class SpringWebSocketApplication

fun main(args: Array<String>) {
    SpringApplication.run(SpringWebSocketApplication::class.java, *args)
}

open class WebSocketConfig: AbstractWebSocketMessageBrokerConfigurer() {

    override fun configureMessageBroker(registry: MessageBrokerRegistry?) =
            registry?.enableSimpleBroker("/contents", "/notification")
                    .unit { registry?.setApplicationDestinationPrefixes("/app") }

    override fun registerStompEndpoints(registry: StompEndpointRegistry?) =
            registry?.addEndpoint("/message")?.withSockJS().asUnit
}

val <T: Any> T?.asUnit: Unit get() = if (this == null) Unit else Unit

fun <T: Any, R> T?.unit(f: (T) -> R): Unit = if (this == null) Unit else f(this).let { Unit }
