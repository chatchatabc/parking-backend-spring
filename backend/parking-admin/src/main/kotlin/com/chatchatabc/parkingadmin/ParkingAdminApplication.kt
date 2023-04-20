package com.chatchatabc.parkingadmin

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableDubbo
class ParkingAdminApplication

fun main(args: Array<String>) {
    runApplication<ParkingAdminApplication>(*args)
}
