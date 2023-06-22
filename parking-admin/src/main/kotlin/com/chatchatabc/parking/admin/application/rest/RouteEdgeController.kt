package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.RouteEdgeMapper
import com.chatchatabc.parking.domain.model.RouteEdge
import com.chatchatabc.parking.domain.routeEdge
import com.chatchatabc.parking.domain.routeNode
import com.chatchatabc.parking.domain.service.RouteEdgeService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.mapstruct.factory.Mappers
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/route-edge")
class RouteEdgeController(
    private val routeEdgeService: RouteEdgeService,
) {
    private val routeEdgeMapper = Mappers.getMapper(RouteEdgeMapper::class.java)

    /**
     * Create Route Edges data class
     */
    data class RouteEdgesCreateRequest(
        val edges: List<RouteEdgeCreateRequest>
    )

    /**
     * Route Edges data class
     */
    data class RouteEdgeCreateRequest(
        val routeId: Long,
        val nodeFrom: Long,
        val nodeTo: Long
    )

    /**
     * Create a Route Edge
     */
    @Operation(
        summary = "Create a Route Edge",
        description = "Create a Route Edge"
    )
    @PostMapping
    fun createEdge(
        @RequestBody request: RouteEdgeCreateRequest,
    ) = runCatching {
        val edge = RouteEdge().apply {
            this.distance = routeEdgeService.calculateDistanceBetweenNodes(
                request.nodeFrom.routeNode,
                request.nodeTo.routeNode
            )
        }
        routeEdgeMapper.createRouteEdgeFromCreateRequest(request, edge)
        routeEdgeService.saveRouteEdge(edge)
    }.getOrElse { it.toErrorResponse() }

    /**
     * Create Route Edges
     */
    @Operation(
        summary = "Create Route Edges",
        description = "Create Route Edges"
    )
    @PostMapping("/many")
    fun createEdges(
        @RequestBody request: RouteEdgesCreateRequest,
    ) = runCatching {
        routeEdgeService.saveRouteEdges(
            request.edges.map {
                val edge = RouteEdge().apply {
                    this.distance = routeEdgeService.calculateDistanceBetweenNodes(
                        it.nodeFrom.routeNode,
                        it.nodeTo.routeNode
                    )
                }
                routeEdgeMapper.createRouteEdgeFromCreateRequest(it, edge)
                edge
            }
        ).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update Route Edges data class
     */
    data class RouteEdgeUpdateRequest(
        val routeId: Long?,
        val nodeFrom: Long?,
        val nodeTo: Long?
    )

    /**
     * Update Route Edge
     */
    @Operation(
        summary = "Update Route Edge",
        description = "Update Route Edge"
    )
    @PutMapping("/{id}")
    fun updateEdge(
        @RequestBody request: RouteEdgeUpdateRequest,
        @PathVariable id: Long,
    ) = runCatching {
        val edge = id.routeEdge.apply {
            this.distance = routeEdgeService.calculateDistanceBetweenNodes(
                request.nodeFrom?.routeNode,
                request.nodeTo?.routeNode
            )
        }
        routeEdgeMapper.updateRouteEdgeFromUpdateRequest(request, edge)
        routeEdgeService.saveRouteEdge(edge).toResponse()
    }.getOrElse { it.toErrorResponse() }
}