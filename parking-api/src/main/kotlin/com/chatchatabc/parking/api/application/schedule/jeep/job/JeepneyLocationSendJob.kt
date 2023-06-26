package com.chatchatabc.parking.api.application.schedule.jeep.job

import com.chatchatabc.parking.web.common.application.NatsMessage
import com.chatchatabc.parking.web.common.application.enums.NatsPayloadTypes
import com.chatchatabc.parking.web.common.application.nats.NatsPayload
import com.chatchatabc.parking.web.common.application.rest.service.GpsRestService
import com.chatchatabc.parking.web.common.application.toJson
import io.nats.client.Connection
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class JeepneyLocationSendJob(
    private val natsConnection: Connection,
    private val gpsRestService: GpsRestService
) : Job {
    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Send Jeepney Location to NATS channel
     */
    override fun execute(context: JobExecutionContext?): Unit = runCatching {
        // API Call get location of jeepney via Azliot
        val data = gpsRestService.carHomeTF
        log.info("JeepneyLocationSendJob Executed")

        // Send location to NATS channel
        data.results.forEach { jeep ->
            NatsMessage(
                NatsPayloadTypes.JEEPNEY_LOCATION_UPDATE,
                NatsPayload.JeepneyPayload(
                    jeep.vkey,
                    jeep.lat,
                    jeep.lng,
                    jeep.dir
                )
            )
                .toJson().toByteArray().runCatching {
                    natsConnection.publish("jeepney-${jeep.vkey}", this)
                    natsConnection.publish("route-${jeep.groupName}", this)
                }
        }
    }.getOrElse { e ->
        log.error("Error in JeepneyLocationSendJob: ${e.message}")
    }
}