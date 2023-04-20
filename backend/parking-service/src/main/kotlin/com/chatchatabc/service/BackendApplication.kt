package com.chatchatabc.service

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableDubbo
class BackendApplication

fun main(args: Array<String>) {
    runApplication<com.chatchatabc.service.BackendApplication>(*args)
}
