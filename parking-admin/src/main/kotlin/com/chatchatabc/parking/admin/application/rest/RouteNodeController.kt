package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.domain.model.RouteNode
import com.chatchatabc.parking.domain.service.RouteNodeService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/route-node")
class RouteNodeController(
    private val routeNodeService: RouteNodeService
) {

    /**
     * Create Route Nodes data class
     */
    data class RouteNodeCreateRequest(
        val nodes: List<RouteNodes>
    )

    /**
     * Route Node data class
     */
    data class RouteNodes(
        val latitude: Double,
        val longitude: Double,
        val poi: String
    )

    /**
     * Create Route Nodes
     */
    @Operation(
        summary = "Create Route Nodes",
        description = "Create Route Nodes"
    )
    @PostMapping
    fun createNodes(
        @RequestBody request: RouteNodeCreateRequest,
    ) = runCatching {
        routeNodeService.saveRouteNodes(
            request.nodes.map {
                RouteNode().apply {
                    this.latitude = it.latitude
                    this.longitude = it.longitude
                    this.poi = it.poi
                }
            }).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update Route Node
     */
    @Operation(
        summary = "Update Route Node",
        description = "Update Route Node"
    )
    @PutMapping
    fun updateNode(
        @RequestBody request: RouteNode,
    ) = runCatching {
        val node = RouteNode().apply {
            this.id = request.id
            this.latitude = request.latitude
            this.longitude = request.longitude
            this.poi = request.poi
        }
        routeNodeService.saveRouteNode(node).toResponse()
    }.getOrElse { it.toErrorResponse() }
}