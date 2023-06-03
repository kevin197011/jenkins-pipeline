package io.devops.utils

import jenkins.model.Jenkins
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob

class JenkinsJob {

//    private String jobName
//    private String jobDSL
//
//    JenkinsJob(String jobName) {
//        this.jobName = jobName
//    }
//
//    JenkinsJob(String jobName, String jobDSL) {
//        this.jobName = jobName
//        this.jobDSL = jobDSL
//    }

    static boolean addOrUpdateJob(String jobName, String jobDSL) {

        Jenkins instance = Jenkins.getInstanceOrNull()

        if (instance == null) {
//            println "Instance is Null!"
            return false
        }

        CpsFlowDefinition flowDefinition = new CpsFlowDefinition(jobDSL, true)
        WorkflowJob job = new WorkflowJob(instance, jobName)
        job.definition = flowDefinition
        job.setConcurrentBuild(false)

//        job.addProperty(new RateLimitBranchProperty.JobPropertyImpl
//                (new RateLimitBranchProperty.Throttle(60, "hours")))
//        def spec = "H 0 1 * *"
//        TimerTrigger newCron = new TimerTrigger(spec)
//        newCron.start(job, true)
//        job.addTrigger(newCron)

        job.save()
        Jenkins.getInstanceOrNull().reload()

        return true
    }

    static boolean deleteJob(String jobName) {
        Jenkins instance = Jenkins.getInstanceOrNull()
        WorkflowJob job = instance.getItem(jobName) as WorkflowJob
        if (job == null) {
//            println "get job is null!"
            return false
        }
        job.delete()
        return true
    }

}
