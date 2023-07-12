package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.RouteNodeMapper
import com.chatchatabc.parking.domain.model.RouteNode
import com.chatchatabc.parking.domain.repository.RouteNodeRepository
import com.chatchatabc.parking.domain.routeNode
import com.chatchatabc.parking.domain.service.RouteNodeService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/route-node")
class RouteNodeController(
    private val routeNodeService: RouteNodeService,
    private val routeNodeRepository: RouteNodeRepository
) {
    private val routeNodeMapper = Mappers.getMapper(RouteNodeMapper::class.java)

    /**
     * Get Route Nodes
     */
    @Operation(
        summary = "Get Route Nodes",
        description = "Get Route Nodes"
    )
    @GetMapping
    fun getRouteNodes(
        pageable: Pageable
    ) = run { routeNodeRepository.findAll(pageable).toResponse() }

    /**
     * Get Route Node by ID
     */
    @Operation(
        summary = "Get Route Node by ID",
        description = "Get Route Node by ID"
    )
    @GetMapping("/{id}")
    fun getRouteNode(@PathVariable id: Long) = run { id.routeNode.toResponse() }

    /**
     * Create Route Nodes data class
     */
    data class RouteNodesCreateRequest(
        val nodes: List<RouteNodeMapper.RouteNodeMapDTO>
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
        @RequestBody request: RouteNodeMapper.RouteNodeMapDTO,
    ) = runCatching {
        val node = RouteNode()
        routeNodeMapper.mapRequestToRouteNode(request, node)
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
                routeNodeMapper.mapRequestToRouteNode(it, node)
                node
            }).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update Route Node
     */
    @Operation(
        summary = "Update Route Node",
        description = "Update Route Node"
    )
    @PutMapping("/{id}")
    fun updateNode(
        @RequestBody request: RouteNodeMapper.RouteNodeMapDTO,
        @PathVariable id: Long,
    ) = runCatching {
        val node = id.routeNode
        routeNodeMapper.mapRequestToRouteNode(request, node)
        routeNodeService.saveRouteNode(node).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update Many Route Nodes data class
     */
    data class RouteNodesUpdateRequest(
        val nodes: List<RouteNodeMapper.RouteNodeMapDTO>
    )

    /**
     * Update Many Route Nodes
     */
    @Operation(
        summary = "Update Many Route Nodes",
        description = "Update Many Route Nodes"
    )
    @PutMapping("/many")
    fun updateNodes(
        @RequestBody request: RouteNodesUpdateRequest,
    ) = runCatching {
        routeNodeService.saveRouteNodes(
            request.nodes.map {
                val node = it.id.routeNode
                routeNodeMapper.mapRequestToRouteNode(it, node)
                node
            }).toResponse()
    }.getOrElse { it.toErrorResponse() }
}