package com.chatchatabc.parking.api.impl.application.event

import com.chatchatabc.parking.api.application.event.member.MemberEventProcessor
import com.chatchatabc.parking.api.application.event.member.MemberLoginEvent
import com.chatchatabc.parking.api.application.schedule.member.MemberSchedule
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class MemberEventProcessorImpl(
    private val memberSchedule: MemberSchedule
) : MemberEventProcessor {
    /**
     * Handle user login event
     */
    @Async
    @EventListener
    override fun handleMemberLoginEvent(event: MemberLoginEvent) {
        val phone = event.phone
        val otp = event.otp
        memberSchedule.onLoginSendSMSOTP(phone, otp)
    }
}