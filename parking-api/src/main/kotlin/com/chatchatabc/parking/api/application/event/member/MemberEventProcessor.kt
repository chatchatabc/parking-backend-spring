package com.chatchatabc.parking.api.application.event.member


interface MemberEventProcessor {

    /**
     * Handle member login event
     */
    fun handleMemberLoginEvent(event: MemberLoginEvent)
}
