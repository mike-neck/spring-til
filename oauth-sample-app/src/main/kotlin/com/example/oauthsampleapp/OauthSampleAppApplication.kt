package com.example.oauthsampleapp

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class OauthSampleAppApplication

fun main(args: Array<String>) {
    SpringApplication.run(OauthSampleAppApplication::class.java, *args)
}
