package com.chatchatabc.parkingenforcer

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableDubbo
class ParkingEnforcerApplication

fun main(args: Array<String>) {
    runApplication<ParkingEnforcerApplication>(*args)
}
