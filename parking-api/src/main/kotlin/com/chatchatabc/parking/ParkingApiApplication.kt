package com.chatchatabc.parking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(DomainConfig::class)
class ParkingManagementApplication

fun main(args: Array<String>) {
    runApplication<ParkingManagementApplication>(*args)
}
