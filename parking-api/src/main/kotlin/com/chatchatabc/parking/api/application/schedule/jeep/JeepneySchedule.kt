package com.chatchatabc.parking.api.application.schedule.jeep

import com.chatchatabc.parking.api.application.schedule.jeep.job.JeepneyLocationSendJob
import com.chatchatabc.parking.web.common.application.config.rest.AzliotRestConfig
import com.chatchatabc.parking.web.common.application.rest.service.GpsRestService
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import org.quartz.JobBuilder
import org.quartz.Scheduler
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class JeepneySchedule(
    private val scheduler: Scheduler,
    private val jwtService: JwtService,
    private val gpsRestService: GpsRestService,

    // Values from .env
    @Value("\${azliot.api.tf-key}")
    private var tfKey: String? = null,
    @Value("\${azliot.api.user-key}")
    private val userKey: String? = null,
    @Value("\${azliot.api.password}")
    private val password: String? = null,
    @Value("\${azliot.api.time}")
    private val time: String? = null
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Job that sends the location of Jeepneys to a NATS channel every 10 seconds
     */
    @Bean
    fun jeepneyLocationSend() {
        val job = JobBuilder
            .newJob(JeepneyLocationSendJob::class.java)
            .withIdentity("jeepneyLocationSend", "jeep")
            .build()
        val trigger = TriggerBuilder
            .newTrigger()
            .withSchedule(
                SimpleScheduleBuilder.repeatSecondlyForever(10)
            )
            .build()
        scheduler.scheduleJob(job, trigger)
    }

    /**
     * Generate Azliot Token on startup
     */
    @Bean
    fun generateToken() = runCatching {
        AzliotRestConfig.AZLIOT_TOKEN = gpsRestService.generateToken(
            tfKey,
            userKey,
            password,
            time
        ).results.token
        log.info("Azliot token generated...")
    }.getOrElse {
        log.error("Error generating initial token: ${it.message}")
    }

    /**
     * Validate Azliot Token every 10 Minutes
     */
    @Bean
    @Scheduled(fixedRate = 10 * 60 * 1000)
    fun validateToken() = runCatching {
        // Buffer time in seconds (10 minutes)
        val bufferTimeInSeconds = 10 * 60

        // If token is expired or about to expire, renew it
        if (AzliotRestConfig.AZLIOT_TOKEN != null) {
            // Get the expiration timestamp from the token
            val exp = jwtService.getExpirationFromToken(AzliotRestConfig.AZLIOT_TOKEN!!)

            // Check if token is expired or about to expire
            if (exp != null && Instant.now().epochSecond + bufferTimeInSeconds >= exp) {
                // Renew token
                log.info("Token expired or about to expire. Renewing...")
                AzliotRestConfig.AZLIOT_TOKEN = gpsRestService.generateToken(
                    tfKey,
                    userKey,
                    password,
                    time
                ).results.token
            }
        }
    }.getOrElse {
        log.error("Error renewing token: ${it.message}")
    }
}