package io.devops.cicd

@Grapes([
        @Grab(group = 'org.wisdom-framework', module = 'mysql-connector-java', version = '5.1.34_1'),
//        @GrabConfig( systemClassLoader=true )
])

import groovy.sql.Sql

class DeployDatabase {

    Script script
    private String project
    private String projectEnv
    private String host
    private String database
    private String username
    private String password

    boolean execute(String project, String projectEnv, String host, String database, String username, String password) {

        this.project = project
        this.projectEnv = projectEnv
        this.host = host
        this.database = database
        this.username = username
        this.password = password

        boolean val = false

        String sqlData = new File("apps-${this.project}/${this.projectEnv}/sql/update.sql").text

        Sql instance = Sql.newInstance("jdbc:mysql://${this.host}:3306/${this.database}?allowMultiQueries=true",
                this.username, this.password, "com.mysql.jdbc.Driver")
        instance.connection.autoCommit = false

        try {
            instance.execute(sqlData)
            instance.commit()
            script.each {"project[${this.project}] env[${this.projectEnv}] sql update sucessed!"}
            val = true
        } catch (Exception ex) {
            instance.rollback()
            script.each {ex.toString()}
//            ex.printStackTrace()
        }
        instance.close()
        return val
    }

}
