package com.chatchatabc.parking.impl.domain.event;

import com.chatchatabc.parking.domain.event.user.UserEventProcessor;
import com.chatchatabc.parking.domain.event.user.UserLoginEvent;
import com.chatchatabc.parking.impl.infra.schedule.user.UserSchedule;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserEventProcessorImpl implements UserEventProcessor {
    @Autowired
    private UserSchedule userSchedule;

    /**
     * Handle user login event
     *
     * @param event the user login event
     */
    @Async
    @EventListener
    @Override
    public void handleUserLoginEvent(UserLoginEvent event) throws SchedulerException {
        String phone = event.getPhone();
        String otp = event.getOtp();
        userSchedule.onLoginSendSMSOTP(phone, otp);
    }
}
