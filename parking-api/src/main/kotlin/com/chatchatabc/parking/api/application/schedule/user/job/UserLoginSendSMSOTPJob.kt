package com.chatchatabc.parking.api.application.schedule.user.job

import org.quartz.Job
import org.quartz.JobExecutionContext

class UserLoginSendSMSOTPJob : Job {

    /**
     * Execute the job of sending sms otp
     */
    override fun execute(context: JobExecutionContext) {
        val dataMap = context.jobDetail.jobDataMap
        val phone = dataMap.getString("phone")
        val otp = dataMap.getString("otp")

        // TODO: Send text message with technology. OTP will print for now
        println("Phone: $phone, OTP: $otp")
    }
}