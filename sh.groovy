def buildJar() {
      echo "Building the jar file"
            sh 'mvn clean install'
}

def runTest() {
     echo "Testing the application"
        sh 'mvn test'
}

def buildImage() {
     withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh 'docker build . -t muffius/demo-repo:jma-3.0'
                        sh 'docker push muffius/demo-repo:jma-3.0'
                    }
}

def devDeploy() {
    def image = 'muffius/demo-repo:jma-3.0'
    def stopCmd = "docker stop \$(docker ps -q --filter ancestor=${image}) || true && docker rm \$(docker ps -a -q --filter ancestor=${image}) || true"
    def dockerCmd = 'docker run -d -p 8080:8080 muffius/demo-repo:jma-3.0'
    def username = "muffius"
    def passwordCredentials = credentials('docker-credentials')
    def dockerLoginCmd = "docker login -u ${username} -p ${passwordCredentials}"

    sshagent(['dev-key']) {
       sh "ssh -o StrictHostKeyChecking=no ubuntu@44.202.39.78 '${stopCmd} && ${dockerLoginCmd} && ${dockerCmd}'"
     //  sh "ssh -o StrictHostKeyChecking=no ubuntu@54.210.104.123 '${stopCmd} && ${dockerLoginCmd} && ${dockerCmd}'" 
    }
}


return this