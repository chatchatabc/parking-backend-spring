package com.chatchatabc.parkingenforcer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ParkingEnforcerApplication

fun main(args: Array<String>) {
    runApplication<ParkingEnforcerApplication>(*args)
}
