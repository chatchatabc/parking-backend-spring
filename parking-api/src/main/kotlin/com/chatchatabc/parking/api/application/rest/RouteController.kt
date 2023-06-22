package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.model.RouteEdge
import com.chatchatabc.parking.domain.model.RouteNode
import com.chatchatabc.parking.domain.repository.RouteNodeRepository
import com.chatchatabc.parking.domain.repository.RouteRepository
import com.chatchatabc.parking.domain.route
import com.chatchatabc.parking.domain.routeEdges
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toNullResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/route")
class RouteController(
    private val routeRepository: RouteRepository,
    private val routeNodeRepository: RouteNodeRepository
) {

    /**
     * Get All Routes
     */
    @Operation(
        summary = "Get All Routes",
        description = "Allow users to get all routes"
    )
    @GetMapping
    fun getAllRoutes(pageable: Pageable) =
        runCatching { routeRepository.findAll(pageable).toResponse() }.getOrElse { it.toErrorResponse() }

    /**
     * Check last updated at of a route
     */
    @Operation(
        summary = "Check last updated at of a route",
        description = "Allow users to check last updated at of a route"
    )
    @GetMapping("/last-updated/{id}/{timestamp}")
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

    /**
     * Route Nodes and Edges data class
     */
    data class RouteNodesAndEdges(
        val nodes: List<RouteNode>,
        val edges: List<RouteEdge>
    )

    /**
     * Get Route nodes and edges by route identifier
     */
    @Operation(
        summary = "Get Route nodes and edges by route identifier",
        description = "Allow users to get route nodes and edges by route identifier"
    )
    @GetMapping("/nodes-and-edges/{id}")
    fun getRouteNodesAndEdgesById(@PathVariable id: String) =
        runCatching {
            val edges = id.route.id.routeEdges
            val nodes = routeNodeRepository.findAllByIdIn(edges.flatMap { listOf(it.nodeFrom, it.nodeTo) }.toSet())
            RouteNodesAndEdges(
                nodes,
                edges
            ).toResponse()
        }.getOrElse { it.toErrorResponse() }
}