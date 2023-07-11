def incrementVersion() {
    echo "incrementing Version"
    sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\${parsedVersion.majorVersion}.\\${parsedVersion.minorVersion}.\\${parsedVersion.nextIncrementalVersion} versions:commit'
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    def VERSION = matcher[0][1]
    env.IMAGE = "${VERSION}-${BUILD_NUMBER}"
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
    def stopCmd = "docker stop \$(docker ps -q --filter label=my-app) || true && docker rm \$(docker ps -a -q --filter label=my-app) || true"
    def dockerCmd = "docker run -d -p 8080:8080 --label my-app ${image}"

    withCredentials([string(credentialsId: 'docker-hub-access-token', variable: 'DOCKERHUB_ACCESS_TOKEN')]) {
        sshagent(['dev-key']) {
            sh """
            ssh -o StrictHostKeyChecking=no ubuntu@3.80.140.21 'docker login -u muffius -p "${DOCKERHUB_ACCESS_TOKEN}" &&
            ${stopCmd} &&
            ${dockerCmd}'"""
        }
    }
}


def commitVersion() {
     withCredentials([usernamePassword(credentialsId:'github-access-token', usernameVariable:'USER', passwordVariable:'PASS' )]) {
                    sh 'git config --global user.email "jenkins@example.com"'
                    sh 'git config --global user.name "jenkins"'
                    sh "git remote set-url origin https://${USER}:${PASS}@github.com/Muffiuss/M-D_Java_Maven_Repo.git"
                    sh 'git add .' 
                    sh 'git commit -m "ci:version bump"'
                    sh 'git push origin HEAD:develop'
               }
}

return this