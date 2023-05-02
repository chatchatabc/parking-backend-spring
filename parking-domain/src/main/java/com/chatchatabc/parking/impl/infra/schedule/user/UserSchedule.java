package com.chatchatabc.parking.impl.infra.schedule.user;

import com.chatchatabc.parking.impl.infra.schedule.user.job.UserLoginSendSMSOTPJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSchedule {
    @Autowired
    private Scheduler scheduler;

    /**
     * Schedule job to send sms otp when user login
     *
     * @param phone the phone number
     * @param otp   the otp
     * @throws SchedulerException if scheduler failed
     */
    public void onLoginSendSMSOTP(String phone, String otp) throws SchedulerException {
        JobDetail job = JobBuilder
                .newJob(UserLoginSendSMSOTPJob.class)
                .withIdentity("onLoginSendSMSOTP", "user")
                .usingJobData("phone", phone)
                .usingJobData("otp", otp)
                .build();
        Trigger trigger = TriggerBuilder.newTrigger().startNow().build();
        scheduler.scheduleJob(job, trigger);
    }
}
