package com.chatchatabc.parking.api.application.schedule

import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.quartz.listeners.JobListenerSupport

class JobListener : JobListenerSupport() {

    /**
     * Get the name of the JobListener.
     */
    override fun getName(): String {
        return "JobListener"
    }

    @Override
    override fun jobToBeExecuted(context: JobExecutionContext) {
        val jobName = context.jobDetail.key.name
        println("JobListener says: Job $jobName is about to be executed.")
    }

    @Override
    override fun jobExecutionVetoed(context: JobExecutionContext) {
        val jobName = context.jobDetail.key.name
        println("JobListener says: Job $jobName is about to be executed.")
    }

    @Override
    override fun jobWasExecuted(context: JobExecutionContext, jobException: JobExecutionException?) {
        val jobName = context.jobDetail.key.name
        println("JobListener says: Job $jobName was executed.")
    }
}