package com.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class SpringWebSocketApplication

fun main(args: Array<String>) {
    SpringApplication.run(SpringWebSocketApplication::class.java, *args)
}
