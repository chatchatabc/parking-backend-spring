package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.repository.RouteRepository
import com.chatchatabc.parking.domain.route
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toNullResponse
import com.chatchatabc.parking.web.common.application.toResponse
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/route")
class RouteController(
    private val routeRepository: RouteRepository
) {

    /**
     * Get All Routes
     */
    @GetMapping("/")
    fun getAllRoutes(pageable: Pageable) =
        runCatching { routeRepository.findAll(pageable).toResponse() }.getOrElse { it.toErrorResponse() }

    /**
     * Check last updated at of a route
     */
    @GetMapping("/last-updated-at/{id}/{timestamp}")
    fun getLastUpdatedAtOfRoute(@PathVariable id: String, @PathVariable timestamp: LocalDateTime) =
        runCatching {
            val route = id.route
            if (route.updatedAt.isAfter(timestamp)) {
                route.toResponse()
            } else {
                // Return empty response
                route.toNullResponse()
            }
        }.getOrElse { it.toErrorResponse() }
}