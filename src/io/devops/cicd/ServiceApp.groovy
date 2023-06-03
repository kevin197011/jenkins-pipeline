package io.devops.cicd

class ServiceApp {

    Script script

    void stop(String project, String app, String host, Boolean init) {
        if (init) {
            script.echo("New deploy, skip app stop!")
            return
        }
        script.sh("bash -x scripts/${project}/${app}/service.sh ${host} ${app} stop")
    }

    void start(String project, String app, String host) {
        script.sh("bash -x scripts/${project}/${app}/service.sh ${host} ${app} start")
    }

    void restart(String project, String app, String host, Boolean init) {
        stop(project, app, host, init)
        start(project, app, host)
    }
}
