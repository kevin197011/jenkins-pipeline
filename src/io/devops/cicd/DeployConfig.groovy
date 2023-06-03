package io.devops.cicd

class DeployConfig {

    Script script

    void deploy(String project, String projectEnv, String app, String host) {
        script.sh("bash -x scripts/${project}/${app}/config.sh ${project} ${projectEnv} ${host} ${app}")
    }
}
