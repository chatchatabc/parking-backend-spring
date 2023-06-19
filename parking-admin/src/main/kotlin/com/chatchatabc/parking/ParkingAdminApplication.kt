package com.chatchatabc.parking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ParkingAdminApplication

fun main(args: Array<String>) {
    runApplication<ParkingAdminApplication>(*args)
}
