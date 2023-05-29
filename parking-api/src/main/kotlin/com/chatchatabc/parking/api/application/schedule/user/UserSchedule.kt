package com.chatchatabc.parking.api.application.schedule.user

import com.chatchatabc.parking.api.application.schedule.user.job.UserLoginSendSMSOTPJob
import org.quartz.JobBuilder
import org.quartz.Scheduler
import org.quartz.TriggerBuilder
import org.springframework.stereotype.Component

@Component
class UserSchedule(
    private val scheduler: Scheduler
) {

    /**
     * Schedule job to send sms otp when user login
     */
    fun onLoginSendSMSOTP(phone: String, otp: String) {
        val job = JobBuilder
            .newJob(UserLoginSendSMSOTPJob::class.java)
            .withIdentity("onLoginSendSMSOTP", "user")
            .usingJobData("phone", phone)
            .usingJobData("otp", otp)
            .build()
        val trigger = TriggerBuilder.newTrigger().startNow().build()
        scheduler.scheduleJob(job, trigger)
    }
}