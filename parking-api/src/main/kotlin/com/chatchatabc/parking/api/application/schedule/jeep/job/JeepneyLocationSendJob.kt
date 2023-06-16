package com.chatchatabc.parking.api.application.schedule.jeep.job

import com.chatchatabc.parking.web.common.application.NatsMessage
import com.chatchatabc.parking.web.common.application.enums.NatsPayloadTypes
import com.chatchatabc.parking.web.common.application.nats.NatsPayload
import com.chatchatabc.parking.web.common.application.rest.service.RestService
import com.chatchatabc.parking.web.common.application.toJson
import com.fasterxml.jackson.databind.ObjectMapper
import io.nats.client.Connection
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JeepneyLocationSendJob(
    private val natsConnection: Connection,
    private val restService: RestService,
    private val objectMapper: ObjectMapper,

    // Values from .env
    @Value("\${azliot.api.tf-key}")
    private val tfKey: String,
    @Value("\${azliot.api.token}")
    private val token: String
) : Job {

    /**
     * Car Home TF Response Data Class
     */
    data class CarHomeTFResponse(
        val results: List<CarResultsItem>,
    )

    /**
     * Car Result Item Data Class
     */
    data class CarResultsItem(
        val carNum: String,
        val groupName: String,
        val vkey: String,
        val deviceId: String,
        val lat: Double,
        val lng: Double,
        val dir: Int
    )

    /**
     * Send Jeepney Location to NATS channel
     */
    override fun execute(context: JobExecutionContext?) {
        // API Call get location of jeepney via Azliot
        val data = restService.makeGetRequest(
            "/Transfer/carHomeTF",
            mapOf(
                "tfKey" to tfKey,
                "Authorization" to token
            ),
            mapOf()
        )

        // Map string json data to data class
        objectMapper.readValue(data, CarHomeTFResponse::class.java).let {
            // Send location to NATS channel
            it.results.forEach { jeep ->
                val natsMessage =
                    NatsMessage(
                        NatsPayloadTypes.JEEPNEY_LOCATION_UPDATE,
                        NatsPayload.JeepneyPayload(
                            jeep.vkey,
                            jeep.lat,
                            jeep.lng,
                            jeep.dir
                        )
                    )
                natsConnection.publish("jeepney-${jeep.vkey}", natsMessage.toJson().toByteArray())
            }
        }
    }
}