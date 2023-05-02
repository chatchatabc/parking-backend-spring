package com.chatchatabc.parking.impl.infra.schedule.user.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

public class UserLoginSendSMSOTPJob implements Job {

    /**
     * Execute the job of sending sms otp
     *
     * @param context the job execution context
     */
    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String phone = dataMap.getString("phone");
        String otp = dataMap.getString("otp");

        // TODO: Send text message with technology. OTP will print for now
        System.out.println("Phone: " + phone + ", OTP: " + otp);
    }
}
