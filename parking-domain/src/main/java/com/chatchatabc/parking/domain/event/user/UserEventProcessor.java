package com.chatchatabc.parking.domain.event.user;

import org.quartz.SchedulerException;

public interface UserEventProcessor {

    /**
     * Handle user login event
     *
     * @param event the user login event
     */
    void handleUserLoginEvent(UserLoginEvent event) throws SchedulerException;
}
