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

def create_script() {
      stage('Create script') {
        steps {
          script {
            def tmpFile = libraryResource 'script1.sh'
            writeFile file: 'script1.sh', text: tmpFile
          }
        }
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
    cleanWs()
    properties()
    deploy(deployArgs)

}
