pipelineJob ('misc/version_bump') {
    description('Version Bump for Azure DevOps repository')

    parameters {[
        stringParam('BRANCH', 'feature/version-bump', 'Target branch Name'),
        stringParam('REPOSITORY', 'DevSecOps/isec-terraform-modules', 'Target repository name')
    ]}

    properties {
        pipelineTriggers {
            triggers {
                pollSCM { 
                    scmpoll_spec('*/2 * * * *')
                }
            }
        }
    }

    definition {
        cpsScm {
            lightweight(false)
            scriptPath('jenkinsfiles/misc/version-bump.groovy')
            scm {
                git {
                    browser()
                    branch('${BRANCH}')
                    remote {
                        credentials('/jenkins/default/azure-devops/repo/isec-jenkins-dsl/ssh-creds')
                        url('indiepay@vs-ssh.visualstudio.com:v3/indiepay/DevSecOps/isec-jenkins-dsl')
                    }
                }
            }
        }
    }
} 
