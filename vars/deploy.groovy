#!/usr/bin/env groovy
// println(System.getProperty("java.ext.dirs"))

import io.devops.cicd.Config
import io.devops.cicd.Gitlab
import io.devops.cicd.DeployDatabase
import io.devops.cicd.DeployApp
import io.devops.cicd.DeployConfig
import io.devops.cicd.ServiceApp


def call() {

    def project = Config.project.join('\n')
    def projectEnv = Config.projectEnv.join('\n')
    def apps = Config.apps
    def appsGit = Config.appsGit
    def configGit = Config.configGit
    def scriptGit = Config.scriptGit
    def hosts = Config.hosts

    def gitlab = new Gitlab(script: this)
    def database = new DeployDatabase(script: this)
    def dbConfig = Config.dbConfig
    def deployApp = new DeployApp(script: this)
    def deployConfig = new DeployConfig(script: this)
    def serviceApp = new ServiceApp(script: this)

    environment {
        ssh_key = credentials('349f9a1e-b4a0-4f1a-98cf-b0574ccf1b54')
    }


    properties([
            parameters([
                    [$class              : 'CascadeChoiceParameter',
                     choiceType          : 'PT_SINGLE_SELECT',
                     description         : 'Which app?',
                     filterLength        : 1,
                     filterable          : false,
                     name                : 'AppName',
                     referencedParameters: 'ProjectName',
                     script              : [
                            $class        : 'GroovyScript',
                            fallbackScript: [
                                    classpath: [],
                                    sandbox  : true,
                                    script   : 'return ["ERROR"]'
                            ],
                            script        : [
                                classpath: [],
                                sandbox  : true,
                                script   : """
                                    def apps = ${apps.inspect()}
                                    return apps[ProjectName]
                                """.stripIndent()
                                ]
                            ]
                    ]
            ])
    ])


    //pipeline
    pipeline {
        agent any

        options {
            timestamps()
            skipDefaultCheckout()
            disableConcurrentBuilds()
            timeout(time: 1, unit: 'HOURS')
        }

        parameters {
            choice(name: 'ProjectName', choices: "${project}", description: 'Which project?')
            choice(name: 'ProjectEnv', choices: "${projectEnv}", description: 'Which projectEnv?')
            booleanParam(name: 'DeployDB', defaultValue: false, description: 'Sure?')
            booleanParam(name: 'Init', defaultValue: false, description: 'Init app?')
            booleanParam(name: 'UpdateJob', defaultValue: true, description: 'Update job?')
        }


        stages {

            stage('Git item') {
                steps {
                    script {
                        if (!params.UpdateJob) {
                            gitlab.clone(appsGit["${params.ProjectName}"], 'master')
                            gitlab.clone(configGit, 'master')
                            gitlab.clone(scriptGit, 'master')
                        }
                    }
                }
            }

            stage('Deploy db') {
                steps {
                    script {
                        if ((!params.UpdateJob) && (params.DeployDB)) {
                            def val = database.execute(
                                params.ProjectName as String,
                                params.ProjectEnv as String,
                                dbConfig["${params.ProjectEnv}"]["${params.ProjectName}"]["host"] as String,
                                dbConfig["${params.ProjectEnv}"]["${params.ProjectName}"]["database"] as String,
                                dbConfig["${params.ProjectEnv}"]["${params.ProjectName}"]["username"] as String,
                                dbConfig["${params.ProjectEnv}"]["${params.ProjectName}"]["password"] as String
                            )
                            if (val) {
                                println("sql execute succeed!")
                            } else {
                                error("sql execute error!")
                            }
                        } else {
                            println("deploy db skip...")
                        }
                    }
                }
            }

            stage('Deploy app') {
                steps {
                    script {
                        if (!params.UpdateJob) {
                            def  host = hosts["${params.ProjectEnv}"]["${params.ProjectName}"]["${params.AppName}"]
                            host.each {
                                serviceApp.stop("${params.ProjectName}", "${params.AppName}", "${it}", params.Init)
                                deployApp.deploy("${params.ProjectName}", "${params.ProjectEnv}", "${params.AppName}", "${it}", params.Init)
//                                serviceApp.start("${params.ProjectName}", "${params.AppName}", "${it}")
                            }
                        }
                    }
                }
            }

            stage('Deploy config') {
                steps {
                    script {
                        if (!params.UpdateJob) {
                            def  host = hosts["${params.ProjectEnv}"]["${params.ProjectName}"]["${params.AppName}"]
                            host.each {
                                deployConfig.deploy("${params.ProjectName}", "${params.ProjectEnv}", "${params.AppName}", "${it}")
//                                serviceApp.restart("${params.ProjectName}", "${params.AppName}", "${it}", params.Init)
                                serviceApp.start("${params.ProjectName}", "${params.AppName}", "${it}")
                            }
                        }
                    }
                }
            }

            stage('Check list') {
                steps {
                    script {
                        // TODO
                        println("check list stage!")
                    }
                }
            }
        }

        post {
            always {
                script {
                    println('always')
                }
            }

            success {
                script {
                    println('success')
                }
            }

            failure {
                script {
                    println('failure')
                }
            }

            aborted {
                script {
                    println('aborted')
                }
            }
        }
    }
}
