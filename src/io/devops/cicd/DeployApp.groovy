package io.devops.cicd

class DeployApp {
    
    Script script

    void deploy(String project, String projectEnv, String app, String host, Boolean init) {
        script.sh("bash -x scripts/${project}/${app}/deploy.sh ${project} ${projectEnv} ${host} ${app} ${init}")
    }

}
