package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.jeepney
import com.chatchatabc.parking.domain.repository.JeepneyRepository
import com.chatchatabc.parking.domain.service.JeepneyService
import com.chatchatabc.parking.web.common.application.NatsMessage
import com.chatchatabc.parking.web.common.application.enums.NatsPayloadTypes
import com.chatchatabc.parking.web.common.application.nats.NatsPayload
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toJson
import com.chatchatabc.parking.web.common.application.toResponse
import io.nats.client.Connection
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/jeepney")
class JeepneyController(
    private val jeepneyRepository: JeepneyRepository,
    private val jeepneyService: JeepneyService,
    private val natsConnection: Connection
) {

    /**
     * Get Jeepney by Identifier
     */
    @Operation(
        summary = "Get Jeepney by Identifier",
        description = "Allow users to get Jeepney by Identifier"
    )
    @GetMapping("/{id}")
    fun getJeepney(@PathVariable id: String) =
        runCatching { id.jeepney.toResponse() }.getOrElse { it.toErrorResponse() }

    /**
     * Get all Jeepneys
     */
    @Operation(
        summary = "Get Jeepneys",
        description = "Allow users to get Jeepneys"
    )
    @GetMapping("/")
    fun getAllJeepneys(
        pageable: Pageable
    ) = runCatching {
        jeepneyRepository.findAll(pageable).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get All Jeepneys by Routes
     */
    @Operation(
        summary = "Get Jeepneys by Routes",
        description = "Allow users to get Jeepneys by Routes"
    )
    @GetMapping("/route/{id}")
    fun getAllJeepneysByRoute(
        @PathVariable id: String,
        pageable: Pageable
    ) = runCatching {
        jeepneyRepository.findAllByRouteUuid(id, pageable).toResponse()
    }.getOrElse { it.toErrorResponse() }

    data class JeepneyUpdateLocationRequest(
        val latitude: Double,
        val longitude: Double,
        val direction: Int
    )

    /**
     * Update Jeepney Location
     */
    // TODO: Might be used for Driver Role. This is not for user and owner role
    @Operation(
        summary = "Update Jeepney Location",
        description = "Allow users to update Jeepney Location"
    )
    @PutMapping("/update-location/{id}")
    fun updateJeepneyLocation(
        @PathVariable id: String,
        request: JeepneyUpdateLocationRequest
    ) = runCatching {
        val jeepney = id.jeepney.apply {
            latitude = request.latitude
            longitude = request.longitude
        }
        jeepneyService.saveJeepney(jeepney)

        // NATS message structure
        val natsMessage =
            NatsMessage(
                NatsPayloadTypes.JEEPNEY_LOCATION_UPDATE,
                NatsPayload.JeepneyPayload(
                    jeepney.jeepneyUuid,
                    jeepney.latitude,
                    jeepney.longitude,
                    request.direction
                )
            ).toJson().toByteArray()

        // Publish to all Clients Listening
        natsConnection.publish("route-${jeepney.routeUuid}", natsMessage)
        jeepney.toResponse()
    }.getOrElse { it.toErrorResponse() }
}