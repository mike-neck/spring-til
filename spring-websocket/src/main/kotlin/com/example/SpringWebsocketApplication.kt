package com.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SpringWebsocketApplication

fun main(args: Array<String>) {
    SpringApplication.run(SpringWebsocketApplication::class.java, *args)
}
