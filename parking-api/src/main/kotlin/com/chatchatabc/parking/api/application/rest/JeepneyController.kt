package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.jeepney
import com.chatchatabc.parking.domain.repository.JeepneyRepository
import com.chatchatabc.parking.web.common.application.NatsMessage
import com.chatchatabc.parking.web.common.application.enums.NatsPayloadTypes
import com.chatchatabc.parking.web.common.application.nats.NatsPayload
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toJson
import com.chatchatabc.parking.web.common.application.toResponse
import io.nats.client.Connection
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/jeepney")
class JeepneyController(
    private val jeepneyRepository: JeepneyRepository,
    private val natsConnection: Connection
) {

    /**
     * Get Jeepney by Identifier
     */
    @GetMapping("/{id}")
    fun getJeepney(@PathVariable id: String) =
        runCatching { id.jeepney.toResponse() }.getOrElse { it.toErrorResponse() }

    /**
     * Get all Jeepneys
     */
    @GetMapping("/all")
    fun getAllJeepneys(
        pageable: Pageable
    ) = runCatching {
        jeepneyRepository.findAll(pageable).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get All Jeepneys by Routes
     */
    @GetMapping("/route/{id}")
    fun getAllJeepneysByRoute(
        @PathVariable id: String,
        pageable: Pageable
    ) = runCatching {
        // TODO: Convert to method extension
        jeepneyRepository.findAllByRouteUuid(id, pageable).toResponse()
    }.getOrElse { it.toErrorResponse() }

    data class JeepneyUpdateLocationRequest(
        val latitude: Double,
        val longitude: Double
    )

    /**
     * Update Jeepney Location
     */
    @PutMapping("/update-location/{id}")
    fun updateJeepneyLocation(
        @PathVariable id: String,
        request: JeepneyUpdateLocationRequest
    ) = runCatching {
        val jeepney = id.jeepney.apply {
            latitude = request.latitude
            longitude = request.longitude
        }
        // TODO: Change to service
        jeepneyRepository.save(jeepney)

        // NATS message structure
        val natsMessage =
            NatsMessage(
                NatsPayloadTypes.JEEPNEY_LOCATION_UPDATE,
                NatsPayload.JeepneyPayload(
                    jeepney.jeepneyUuid,
                    jeepney.latitude,
                    jeepney.longitude
                )
            ).toJson().toByteArray()

        // Publish to all Clients Listening
        natsConnection.publish("route-${jeepney.routeUuid}", natsMessage)
        jeepney.toResponse()
    }.getOrElse { it.toErrorResponse() }

}