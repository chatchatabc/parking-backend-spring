package com.chatchatabc.parking.api.application.schedule.jeep

import com.chatchatabc.parking.api.application.schedule.jeep.job.JeepneyLocationSendJob
import org.quartz.JobBuilder
import org.quartz.Scheduler
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class JeepneySchedule(
    private val scheduler: Scheduler
) {

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
}