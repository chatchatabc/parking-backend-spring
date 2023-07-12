package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.JeepneyMapper
import com.chatchatabc.parking.domain.jeepney
import com.chatchatabc.parking.domain.model.Jeepney
import com.chatchatabc.parking.domain.repository.JeepneyRepository
import com.chatchatabc.parking.domain.route
import com.chatchatabc.parking.domain.service.JeepneyService
import com.chatchatabc.parking.domain.specification.JeepneySpecification
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/jeepney")
class JeepneyController(
    private val jeepneyService: JeepneyService,
    private val jeepneyRepository: JeepneyRepository
) {
    private val jeepneyMapper = Mappers.getMapper(JeepneyMapper::class.java)

    /**
     * Get Jeepneys
     */
    @Operation(
        summary = "Get Jeepneys",
        description = "Get Jeepneys"
    )
    @GetMapping
    fun getJeepneys(
        pageable: Pageable,
        @RequestParam params: Map<String, String>
    ) = run {
        val spec = JeepneySpecification.withKeyword(params["keyword"] ?: "")
        jeepneyRepository.findAll(spec, pageable).toResponse()
    }

    /**
     * Get Jeepneys by Identifier
     */
    @Operation(
        summary = "Get Jeepneys by Identifier",
        description = "Get Jeepneys by Identifier"
    )
    @GetMapping("/{id}")
    fun getJeepney(@PathVariable id: String) = run { id.jeepney.toResponse() }

    /**
     * Get Jeepneys by Route
     */
    @Operation(
        summary = "Get Jeepneys by Route",
        description = "Get Jeepneys by Route"
    )
    @GetMapping("/route/{id}")
    fun getJeepneysByRoute(
        @PathVariable id: String,
        pageable: Pageable
    ) = run {
        jeepneyRepository.findAllByRouteUuid(id.route.routeUuid, pageable).toResponse()
    }

    /**
     * Create a new Jeepney
     */
    @Operation(
        summary = "Create a new Jeepney",
        description = "Create a new Jeepney"
    )
    @PostMapping
    fun create(
        @RequestBody req: JeepneyMapper.JeepneyMapDTO
    ) = runCatching {
        val jeepney = Jeepney()
        jeepneyMapper.mapRequestToJeepney(req, jeepney)
        jeepneyService.saveJeepney(jeepney).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update a Jeepney
     */
    @Operation(
        summary = "Update a Jeepney",
        description = "Update a Jeepney"
    )
    @PutMapping("/{id}")
    fun update(
        @RequestBody req: JeepneyMapper.JeepneyMapDTO,
        @PathVariable id: String
    ) = runCatching {
        val jeepney = id.jeepney
        jeepneyMapper.mapRequestToJeepney(req, jeepney)
        jeepneyService.saveJeepney(jeepney).toResponse()
    }.getOrElse { it.toErrorResponse() }
}