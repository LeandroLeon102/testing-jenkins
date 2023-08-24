pipeline {
    agent any

    stages {
        stage('checkout repository') {
            steps {
                cleanWs()
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

        stage('version bump'){
            steps {
                dir('repo') {
                    sh 'git log --format=oneline'
                    sh 'git tag'
                    script {
                        def status = sh(returnStatus: true, script: "cz bump")
                        if (status != 0) {
                            sh 'git commit --allow-empty -m "fix: force patch version bump - no valid action in last commit message"'
                            sh 'cz bump'
                        }
                    }
                    sh 'git log --format=oneline'
                    sh 'git tag'
                }
            }
        }
    }
}