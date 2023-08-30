pipelineJob ('') {
    description('')

    parameters {[
        stringParam('BRANCH', '', 'Target branch Name'),
        stringParam('REPOSITORY', '', 'Target repository name')
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
            scriptPath('')
            scm {
                git {
                    browser()
                    branch('${BRANCH}')
                    remote {
                        credentials('/')
                        url('')
                    }
                }
            }
        }
    }
} 
