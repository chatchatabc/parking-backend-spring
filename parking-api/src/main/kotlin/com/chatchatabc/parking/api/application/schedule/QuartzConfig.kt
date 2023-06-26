package com.chatchatabc.parking.api.application.schedule

import org.quartz.Scheduler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QuartzConfig(
    private val scheduler: Scheduler
) {

    /**
     * Add listener to scheduler
     */
//    @Bean
//    fun quartzListenerSetup() {
//        scheduler.listenerManager.addJobListener(JobListener())
//    }
}