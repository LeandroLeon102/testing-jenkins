pipeline {
    agent any


    stages {
        stage('checkout repository') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[
                        name: '*/main'
                    ]],
                    userRemoteConfigs: [[
                        url: 'https://github.com/LeandroLeon102/testing-jenkins'
                    ]]
                ])
            }
        }
    }
}