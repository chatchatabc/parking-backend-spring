package com.chatchatabc.parking.api.impl.application.event

import com.chatchatabc.parking.api.application.event.user.UserEventProcessor
import com.chatchatabc.parking.api.application.event.user.UserLoginEvent
import com.chatchatabc.parking.api.application.schedule.user.UserSchedule
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class UserEventProcessorImpl(
    private val userSchedule: UserSchedule
) : UserEventProcessor {
    /**
     * Handle user login event
     */
    @Async
    @EventListener
    override fun handleUserLoginEvent(event: UserLoginEvent) {
        val phone = event.phone
        val otp = event.otp
        userSchedule.onLoginSendSMSOTP(phone, otp)
    }
}