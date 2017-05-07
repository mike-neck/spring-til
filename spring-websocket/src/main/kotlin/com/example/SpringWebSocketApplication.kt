package com.example

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry

@SpringBootApplication
class SpringWebSocketApplication

fun main(args: Array<String>) {
    SpringApplication.run(SpringWebSocketApplication::class.java, *args)
}

@EnableWebSocketMessageBroker
@Configuration
class WebSocketConfig: AbstractWebSocketMessageBrokerConfigurer() {

    override fun configureMessageBroker(registry: MessageBrokerRegistry?) =
            registry?.enableSimpleBroker("/contents", "/notification")
                    .unit { registry?.setApplicationDestinationPrefixes("/app") }

    override fun registerStompEndpoints(registry: StompEndpointRegistry?) =
            registry?.addEndpoint("/message")?.withSockJS().asUnit
}

val <T: Any> T?.asUnit: Unit get() = if (this == null) Unit else Unit

fun <T: Any, R> T?.unit(f: (T) -> R): Unit = if (this == null) Unit else f(this).let { Unit }

inline fun <reified T: Any> logger(): Logger = LoggerFactory.getLogger(T::class.java)
