pipeline {

agent any

tools {
     maven 'Maven'
}
   stages {
       
       stage('Build the app') {
          steps {
               script {
                    echo "Building the java maven application"
                    sh 'mvn clean package'
               }
          }
       }

       stage('test') {
         steps {
              script {
               echo "Testing the application"
               sh 'mvn test'
              }
         }

       }
       stage('Build Image') {
          steps {
               script {
                    withCredentials([usernamePassword(credentialId: 'docker-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                     sh "echo $PASS | docker login -u $USER --password-stdin "
                     sh 'docker build . -t muffius/demo-repo:javamavenapp-1.0'
                     sh 'docker push muffius/demo-repo:javamavenapp-1.0'
                    }
               }
          }
       }

       stage('Deploy to Dev') {
          steps {
               scripts {
                    def dockerCmd = 'docker run -d -p 8080:8080 muffius/demo-repo:javamavenapp-1.0'
                    sshagent(['dev-deployment-key']) {
                       sh "ssh -o StrictHostKeyChecking=no ubuntu@44.212.16.207 ${dockerCmd}"
                   }
               }
          }
       }


   }


} 