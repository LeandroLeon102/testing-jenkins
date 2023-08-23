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

        stage('run gitversion') {
            steps {
                dir('repo') {
                    sh 'gitversion -output buildserver'
                    script {
                        def props = readProperties file: 'gitversion.properties'
                        env.GitVersion_SemVer = props.GitVersion_SemVer
                        env.GitVersion_BranchName = props.GitVersion_BranchName
                        env.GitVersion_AssemblySemVer = props.GitVersion_AssemblySemVer
                        env.GitVersion_MajorMinorPatch = props.GitVersion_MajorMinorPatch
                        env.GitVersion_Sha = props.GitVersion_Sha
                    }
                }
            }
        }

        stage('version bump') {
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
                dir('repo') {
                    sh """  
                        git tag "${GitVersion_MajorMinorPatch}" -m "release version ${GitVersion_MajorMinorPatch}"
                        git push origin --tags
                    """
                }
            }
        }
    }
}
