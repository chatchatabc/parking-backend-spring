package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.RouteNodeMapper
import com.chatchatabc.parking.domain.model.RouteNode
import com.chatchatabc.parking.domain.routeNode
import com.chatchatabc.parking.domain.service.RouteNodeService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.mapstruct.factory.Mappers
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/route-node")
class RouteNodeController(
    private val routeNodeService: RouteNodeService
) {
    private val routeNodeMapper = Mappers.getMapper(RouteNodeMapper::class.java)

    /**
     * Create Route Nodes data class
     */
    data class RouteNodesCreateRequest(
        val nodes: List<RouteNodeCreateRequest>
    )

    /**
     * Route Node data class
     */
    data class RouteNodeCreateRequest(
        val latitude: Double,
        val longitude: Double,
        val poi: String
    )

    /**
     * Create a Route Node
     */
    @Operation(
        summary = "Create a Route Node",
        description = "Create a Route Node"
    )
    @PostMapping
    fun createNode(
        @RequestBody request: RouteNodeCreateRequest,
    ) = runCatching {
        val node = RouteNode()
        routeNodeMapper.createRouteNodeFromCreateRequest(request, node)
        routeNodeService.saveRouteNode(node).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Create Route Nodes
     */
    @Operation(
        summary = "Create Route Nodes",
        description = "Create Route Nodes"
    )
    @PostMapping("/many")
    fun createNodes(
        @RequestBody request: RouteNodesCreateRequest,
    ) = runCatching {
        routeNodeService.saveRouteNodes(
            request.nodes.map {
                val node = RouteNode()
                routeNodeMapper.createRouteNodeFromCreateRequest(it, node)
                node
            }).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update Route Node data class
     */
    data class RouteNodeUpdateRequest(
        val latitude: Double?,
        val longitude: Double?,
        val poi: String?
    )

    /**
     * Update Route Node
     */
    @Operation(
        summary = "Update Route Node",
        description = "Update Route Node"
    )
    @PutMapping("/{id}")
    fun updateNode(
        @RequestBody request: RouteNodeUpdateRequest,
        @PathVariable id: Long,
    ) = runCatching {
        val node = id.routeNode
        routeNodeMapper.updateRouteNodeFromUpdateRequest(request, node)
        routeNodeService.saveRouteNode(node).toResponse()
    }.getOrElse { it.toErrorResponse() }
}