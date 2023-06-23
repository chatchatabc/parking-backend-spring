package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.RouteMapper
import com.chatchatabc.parking.domain.model.Route
import com.chatchatabc.parking.domain.route
import com.chatchatabc.parking.domain.service.RouteService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.mapstruct.factory.Mappers
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/route")
class RouteController(
    private val routeService: RouteService
) {
    private val routeMapper = Mappers.getMapper(RouteMapper::class.java)

    data class RouteCreateRequest(
        val slug: String,
        val name: String,
        val description: String,
        val status: Int
    )

    /**
     * Create Route
     */
    @Operation(
        summary = "Create Route",
        description = "Create Route"
    )
    @PostMapping
    fun createRoute(request: RouteCreateRequest) = runCatching {
        val route = Route()
        routeMapper.createRouteFromCreateRequest(request, route)
        routeService.saveRoute(route).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update Route Data Class
     */
    data class RouteUpdateRequest(
        val slug: String?,
        val name: String?,
        val description: String?,
        val status: Int?
    )

    /**
     * Update Route
     */
    @Operation(
        summary = "Update Route",
        description = "Update Route"
    )
    @PutMapping("/{id}")
    fun updateRoute(@PathVariable id: String, @RequestBody request: RouteUpdateRequest) = runCatching {
        val route = id.route
        routeMapper.updateRouteFromUpdateRequest(request, route)
        routeService.saveRoute(route).toResponse()
    }.getOrElse { it.toErrorResponse() }
}