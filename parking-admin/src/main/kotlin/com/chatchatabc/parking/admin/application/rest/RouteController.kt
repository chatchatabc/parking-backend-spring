package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.RouteMapper
import com.chatchatabc.parking.domain.model.Route
import com.chatchatabc.parking.domain.model.RouteEdge
import com.chatchatabc.parking.domain.model.RouteNode
import com.chatchatabc.parking.domain.repository.RouteEdgeRepository
import com.chatchatabc.parking.domain.repository.RouteNodeRepository
import com.chatchatabc.parking.domain.route
import com.chatchatabc.parking.domain.service.RouteService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/route")
class RouteController(
    private val routeService: RouteService,
    private val routeNodeRepository: RouteNodeRepository,
    private val routeEdgeRepository: RouteEdgeRepository
) {
    private val routeMapper = Mappers.getMapper(RouteMapper::class.java)

    data class RouteCreateRequest(
        val slug: String,
        val name: String,
        val description: String,
        val status: Int = Route.RouteStatus.DRAFT
    )

    /**
     * Create Route
     */
    @Operation(
        summary = "Create Route",
        description = "Create Route"
    )
    @PostMapping
    fun createRoute(
        @RequestBody request: RouteCreateRequest
    ) = runCatching {
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

    /**
     * Route Go Request data class
     */
    data class RouteGoRequest(
        val startLatitude: Double,
        val startLongitude: Double,
        val endLatitude: Double,
        val endLongitude: Double
    )

    /**
     * Route Go Response data class
     */
    data class RouteGoResponse(
        val inputLocation: RouteNodeInput,
        val outputLocation: RouteNodeInput,
        val startRouteNode: RouteNode,
        val endRouteNode: RouteNode,
        val routeOptions: List<RouteOption>?
    )

    data class RouteOption(
        val name: String,
        val routeEdges: List<RouteEdge>,
        val duration: String,
        val distance: String,
        val cost: String
    )

    data class RouteNodeInput(
        val latitude: Double,
        val longitude: Double
    )

    /**
     * Get routes based on start location and end location
     */
    @PostMapping("/go")
    fun go(
        @RequestBody req: RouteGoRequest,
    ) = runCatching {
        // Find the closest node to start location
        val pr = PageRequest.of(0, 1)
        val startRouteNode =
            routeNodeRepository.findClosestNode(req.startLatitude, req.startLongitude, pr).content.first()

        // Find closes node to end location
        val endRouteNode = routeNodeRepository.findClosestNode(req.endLatitude, req.endLongitude, pr).content.first()

        // TODO: Optimize this
        val allRouteEdges = routeEdgeRepository.findAll()

        // TODO: Loop through the edges and find all possible rides to the end node
        val maxDepth = 1000000

        val paths = routeService.findAllPaths(allRouteEdges, startRouteNode.id, endRouteNode.id, maxDepth)
        val routeOptions = mutableListOf<RouteOption>()

        // Process each path into route options
        // println("All paths from ${startRouteNode.id} to ${endRouteNode.id}:")
        for ((counter, path) in paths.withIndex()) {
            // TODO: Optimize this
            val routeEdges = path.map { allRouteEdges.first { edge -> edge.id == it } }
            // Sum of the distance of all edges. Round to 2 decimal places
            val distance = routeEdges.sumOf { it.distance } / 1000
            // println(path.joinToString(" -> "))

            routeOptions.add(
                RouteOption(
                    name = "Option $counter",
                    routeEdges = routeEdges,
                    duration = "1 hour MANUAL", // TODO: Calculate duration
                    // Sum of the distance of all edges, convert to km
                    distance = "$distance km",
                    cost = "${routeService.calculateCost(distance)} pesos"
                )
            )
        }
        // println("Options length: ${paths.size}")

        RouteGoResponse(
            inputLocation = RouteNodeInput(req.startLatitude, req.startLongitude),
            outputLocation = RouteNodeInput(req.endLatitude, req.endLongitude),
            startRouteNode = startRouteNode,
            endRouteNode = endRouteNode,
            routeOptions
        ).toResponse()

    }.getOrElse { it.toErrorResponse() }
}