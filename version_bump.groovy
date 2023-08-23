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
                    extensions: [cloneOption(honorRefspec: true, noTags: false, reference: 'repo', shallow: false), localBranch('main'), pruneStaleBranch(), [$class: 'RelativeTargetDirectory', relativeTargetDir: 'repo']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/LeandroLeon102/testing-jenkins'
                    ]]
                ])
            }
        }


        stage('run gitversion') {
            steps {
                dir('repo') {
                    sh 'pwd; ls'
                    sh 'gitversion'
                }

            }
        }
    }
}