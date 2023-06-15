package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.repository.RouteRepository
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}