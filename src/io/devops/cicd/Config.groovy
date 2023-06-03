package io.devops.cicd

class Config {
    // project name
    static final ArrayList<String> project = ['project01', 'project01']

    // project env
    static final ArrayList<String> projectEnv = ['dev', 'prod']

    // project app
    static final LinkedHashMap<String, ArrayList<String>> apps = [
            'project01' : [
                    'project01_app01',
                    'project01_app02',
                    'project01_app03',
            ],
            'project02' : [
                    'project02_app01',
                    'project02_app02',
                    'project03_app03',
            ],
    ]

    // appsGit gitlab
    static final LinkedHashMap<String, String> appsGit = [
            'repo01' : 'git@github.com:kevin197011/repo01.git',
            'repo02' : 'git@github.com:kevin197011/repo02.git',
            'repo03' : 'git@github.com:kevin197011/repo03.git'
    ]


    // configGit gitlab
    static final String configGit = 'git@github.com:kevin197011/config.git'

    // scriptGit gitlab
    static final String scriptGit = 'git@github.com:kevin197011/scripts.git'



    // app host
    static final LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>>> hosts = [
            'dev' : [
                    'project01' : [
                            'project01_app01'     : ['1.1.1.1', '1.1.1.2'],
                            'project01_app02'     : ['1.1.1.1', '1.1.1.2'],
                            'project01_app03'     : ['1.1.1.1', '1.1.1.2'],
                    ],
                    'project02': [
                            'project02_app01'     : ['1.1.1.1', '1.1.1.2'],
                            'project02_app02'     : ['1.1.1.1', '1.1.1.2'],
                            'project02_app03'     : ['1.1.1.1', '1.1.1.2'],
                    ],
            ],

            'prod': [
                    'project01' : [
                            'project01_app01'     : ['1.1.1.1', '1.1.1.2'],
                            'project01_app02'     : ['1.1.1.1', '1.1.1.2'],
                            'project01_app03'     : ['1.1.1.1', '1.1.1.2'],
                    ],
                    'project02': [
                            'project02_app01'     : ['1.1.1.1', '1.1.1.2'],
                            'project02_app02'     : ['1.1.1.1', '1.1.1.2'],
                            'project02_app03'     : ['1.1.1.1', '1.1.1.2'],
                    ],
            ]
    ]

    // db config
    static final LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> dbConfig = [
            'dev': [
                    'project01' : [
                            'username': 'root',
                            'password': 'passwd',
                            'database': 'test',
                            'host'    : '1.1.1.1'
                    ],
                    'project02': [
                            'username': 'root',
                            'password': 'passwd',
                            'database': 'test',
                            'host'    : '1.1.1.1'
                    ],
            ],

            'prod': [
                    'project01' : [
                            'username': 'root',
                            'password': 'passwd',
                            'database': 'test',
                            'host'    : '1.1.1.1'
                    ],
                    'project02': [
                            'username': 'root',
                            'password': 'passwd',
                            'database': 'test',
                            'host'    : '1.1.1.1'
                    ],

            ]
    ]
}
