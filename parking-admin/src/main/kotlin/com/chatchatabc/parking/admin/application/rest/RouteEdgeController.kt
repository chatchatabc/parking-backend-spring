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
        val edges: List<RouteEdgeMapper.RouteEdgeMapDTO>
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
        @RequestBody request: RouteEdgeMapper.RouteEdgeMapDTO,
    ) = runCatching {
        val edge = RouteEdge().apply {
            this.distance = routeEdgeService.calculateDistanceBetweenNodes(
                request.nodeFrom.routeNode,
                request.nodeTo.routeNode
            )
        }
        routeEdgeMapper.mapRequestToRouteEdge(request, edge)
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
                routeEdgeMapper.mapRequestToRouteEdge(it, edge)
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
        @RequestBody request: RouteEdgeMapper.RouteEdgeMapDTO,
        @PathVariable id: Long,
    ) = runCatching {
        val edge = id.routeEdge.apply {
            this.distance = routeEdgeService.calculateDistanceBetweenNodes(
                request.nodeFrom?.routeNode,
                request.nodeTo?.routeNode
            )
        }
        routeEdgeMapper.mapRequestToRouteEdge(request, edge)
        routeEdgeService.saveRouteEdge(edge).toResponse()
    }.getOrElse { it.toErrorResponse() }

    data class RouteEdgesUpdateRequest(
        val edges: List<RouteEdgeMapper.RouteEdgeMapDTO>
    )

    /**
     * Update Many Route Edges
     */
    @Operation(
        summary = "Update Many Route Edges",
        description = "Update Many Route Edges"
    )
    @PutMapping("/many")
    fun updateEdges(
        @RequestBody request: RouteEdgesUpdateRequest
    ) = runCatching {
        routeEdgeService.saveRouteEdges(
            request.edges.map {
                val edge = it.id.routeEdge.apply {
                    this.distance = routeEdgeService.calculateDistanceBetweenNodes(
                        it.nodeFrom?.routeNode,
                        it.nodeTo?.routeNode
                    )
                }
                routeEdgeMapper.mapRequestToRouteEdge(it, edge)
                edge
            }).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     *  Delete Route Edges
     */
    data class RouteEdgesDeleteRequest(
        val ids: List<Long>
    )

    /**
     * Delete Route Edges
     */
    @Operation(
        summary = "Delete Route Edges",
        description = "Delete Route Edges"
    )
    @DeleteMapping("/many")
    fun deleteEdges(
        @RequestBody request: RouteEdgesDeleteRequest
    ) = runCatching {
        routeEdgeService.deleteRouteEdges(request.ids).toResponse()
    }.getOrElse { it.toErrorResponse() }
}