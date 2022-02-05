library identifier: 'deploy@main', retriever: legacySCM(
    [
        $class: 'GitSCM',
        branches: [[name: '*/main']],
        doGenerateSubmoduleConfigurations: false,
        extensions: [],
        submoduleCfg: [],
        userRemoteConfigs: [
           [
             url: 'https://github.com/patsevanton/deploy.git'
           ]
        ]
    ])


def properties() {
    stage ('Set properties') {
        properties([
           disableConcurrentBuilds(),
           parameters([
             string(defaultValue: 'dev', description: '', name: 'BRANCH', trim: false),
            ])
        ])
    }
}

def deploy(Map args) {

    stage('Deploy ') {
        sshagent([args.sshCredId]) {
            withEnv([
                    "SSH_USER=${args.sshUser}",
            ]) {
                    sh '''
		       hostname
		       id
		       pwd
		       ls
                       cat scripts/test.sh
			                 '''
            }
        }
    }
    return this
}


def deployArgs = [
       sshUser: "apatsev",
       sshCredId: "id-ssh-key",
       clearBase: "CLEAR",
       ]
        
node ('agent') {
    properties()
    deploy(deployArgs)

}
