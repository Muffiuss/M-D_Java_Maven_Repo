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
     def dockerCmd = 'docker run -d -p 8080:8080 muffius/demo-repo:jma-3.0'
                     sshagent(['dev-key']) {
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@44.202.39.78 ${dockerCmd}"
                }
}

return this