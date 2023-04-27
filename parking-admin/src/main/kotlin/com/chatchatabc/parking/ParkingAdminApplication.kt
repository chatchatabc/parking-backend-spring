package com.chatchatabc.parking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(DomainConfig::class)
class ParkingAdminApplication

fun main(args: Array<String>) {
    runApplication<ParkingAdminApplication>(*args)
}
