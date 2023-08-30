pipeline {
    agent {label ""}
    options {buildDiscarder(logRotator(daysToKeepStr: "7", numToKeepStr: "10"))}

    stages{
        stage('Checkout repository'){
            steps {
                checkout scm: [
                    $class: 'GitSCM',
                    branches: [[name: params.BRANCH]],
                    extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: params.REPOSITORY]],
                    userRemoteConfigs: [[
                        credentialsId: '',
                        url: "/${REPOSITORY}"
                    ]]
                ]
            }
        }

        stage('version bump'){
            steps {
                dir(params.REPOSITORY) {
                    script {
                        def status = sh(returnStatus: true, script: "cz bump")
                        if (status == 21) {
                            sh """
                                echo 'Last Commit message has no valid action for version bump. Forcing minor version bump...'
                            """
                            sh 'git commit --allow-empty -m "fix: force patch version bump - no valid action in last commit message"'
                            sh 'cz bump'
                        }
                    }
                }
            }
        }
    }
    
    post {
        success {
            dir(params.REPOSITORY) {
                sshagent (credentials: [']) {
                    sh("git checkout ${BRANCH}")
                    sh("git push origin")
                    sh('git push origin --tags')
                }
            }
        }
    }
}
  
