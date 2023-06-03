package io.devops.cicd

class Gitlab {

    Script script

    void clone(String repo, String branch = 'master') {
        String repoName = repo.split('/').last().toString().tokenize('.').first().toString()
        script.echo("git clone ${repoName}!")
        script.sh("rm -rf ${repoName}")
        script.sh("git clone --branch ${branch} ${repo} ${repoName}")
    }
}
