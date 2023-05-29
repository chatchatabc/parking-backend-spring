package com.chatchatabc.parking.api.application.event.user


interface UserEventProcessor {

    /**
     * Handle user login event
     */
    fun handleUserLoginEvent(event: UserLoginEvent)
}
