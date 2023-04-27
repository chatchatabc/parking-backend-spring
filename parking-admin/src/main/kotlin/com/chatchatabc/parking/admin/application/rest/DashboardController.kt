package com.chatchatabc.parking.admin.application.rest

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/dashboard")
class DashboardController {
    @GetMapping("/test")
    fun test(): String {
        return "Hello World!"
    }
}