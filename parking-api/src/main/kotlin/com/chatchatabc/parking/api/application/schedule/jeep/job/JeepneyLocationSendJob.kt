package com.chatchatabc.parking.api.application.schedule.jeep.job

import com.chatchatabc.parking.domain.repository.JeepneyRepository
import com.chatchatabc.parking.domain.route
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
import kotlin.random.Random

@Component
class JeepneyLocationSendJob(
    private val natsConnection: Connection,
    private val gpsRestService: GpsRestService,
    private val jeepneyRepository: JeepneyRepository
) : Job {
    private val log = LoggerFactory.getLogger(this::class.java)
    override fun execute(p0: JobExecutionContext?) = runCatching {
        val jeepneys = jeepneyRepository.findAll()
        log.info("JeepneyLocationSendJob Executed")

        // Generate random location for testing, 7.110345,125.635304
        // random latitude
        val lat = Random.nextDouble(7.11, 7.12)
        // random longitude
        val lng = Random.nextDouble(125.63, 125.64)
        val dir = Random.nextInt(0, 360)

        jeepneys.forEach { jeep ->
            NatsMessage(
                NatsPayloadTypes.JEEPNEY_LOCATION_UPDATE,
                NatsPayload.JeepneyPayload(
                    jeep.jeepneyUuid,
                    lat,
                    lng,
                    dir
                )
            )
                .toJson().toByteArray().runCatching {
                    natsConnection.publish("jeepney-${jeep.jeepneyUuid}", this)
                    natsConnection.publish("route-${jeep.routeUuid.route.slug}", this)
                }
        }
    }.getOrElse { e ->
        log.error("Error in JeepneyLocationSendJob: ${e.message}")
    }

//    /**
//     * Send Jeepney Location to NATS channel
//     */
//    override fun execute(context: JobExecutionContext?): Unit = runCatching {
//        // API Call get location of jeepney via Azliot
//        val data = gpsRestService.carHomeTF
//        log.info("JeepneyLocationSendJob Executed")
//
//        // Send location to NATS channel
//        data.results.forEach { jeep ->
//            NatsMessage(
//                NatsPayloadTypes.JEEPNEY_LOCATION_UPDATE,
//                NatsPayload.JeepneyPayload(
//                    jeep.vkey,
//                    jeep.lat,
//                    jeep.lng,
//                    jeep.dir
//                )
//            )
//                .toJson().toByteArray().runCatching {
//                    natsConnection.publish("jeepney-${jeep.vkey}", this)
//                    natsConnection.publish("route-${jeep.groupName}", this)
//                }
//        }
//    }.getOrElse { e ->
//        log.error("Error in JeepneyLocationSendJob: ${e.message}")
//    }
}