def incrementVersion() {
    echo "incrementing Version"
     sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\${parsedVersion.majorVersion}.\\${parsedVersion.minorVersion}.\\${parsedVersion.nextIncrementalVersion} versions:commit'
          def matcher = readFile('pom.xml')=~ '<version>(.+)</version>'
          def VERSION = matcher[0][1]
          env.IMAGE = ${VERSION}-${BUILD_NUMBER}
}
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
                        sh "docker build . -t ${DOCKER_REPO}:${env.IMAGE}"
                        sh "docker push ${DOCKER_REPO}:${env.IMAGE}"
                    }
}

def devDeploy() {
    def image = "${DOCKER_REPO}:${env.IMAGE}"
    def stopCmd = "docker stop \$(docker ps -q --filter ancestor=${image}) || true && docker rm \$(docker ps -a -q --filter ancestor=${image}) || true"
    def dockerCmd = "docker run -d -p 8080:8080 ${image}"

    withCredentials([string(credentialsId: 'docker-hub-access-token', variable: 'DOCKERHUB_ACCESS_TOKEN')]) {
        sshagent(['dev-key']) {
            sh """ ssh -o StrictHostKeyChecking=no ubuntu@18.206.126.176 'docker login -u muffius -p "${DOCKERHUB_ACCESS_TOKEN}" && ${stopCmd} && ${dockerCmd}'"""
        // sh """ ssh -o StrictHostKeyChecking=no ubuntu@54.210.104.123 'docker login -u muffius -p "${DOCKERHUB_ACCESS_TOKEN}" && ${stopCmd} && ${dockerCmd}'"""
        }  
    }
}

def commitVersion() {
     withCredentials([usernamePassword(credentialsId:'github-credentials', usernameVariable:'USER',passwordVariable:'PASS')]) {
                    sh "git remote set-url origin https://${USER}:${PASS}@github.com/Muffiuss/M-D_Java_Maven_Repo.git"
                    sh 'git add .' 
                    sh 'git commit -m "ci:version bump"'
                    sh 'git push origin HEAD:main'
               }
}


return this