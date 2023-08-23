pipeline {
    agent any
    
    options {}
    
    stages {
        stage('checkout repository') {
            step {
                checkout([
                    $class: 'GitSCM',
                    branches: [[
                        name: '*/main'
                    ]],
                    userRemoteConfigs: [[
                        url: 'https://https://github.com/LeandroLeon102/testing-jenkins'
                    ]]
                ])
            }
        }
    }
}