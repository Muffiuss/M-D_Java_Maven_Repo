pipeline {
    agent any 

    tools {
        maven 'Maven'
    }
    
    environment {
        DOCKER_CMD = 'docker run -d -p 8080:8080 muffius/demo-repo:jma-3.0'
    }
    
    stages {
        stage('Build App') {
            steps {
                script {
                    echo "Building the jar file"
                    sh 'mvn clean install'
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
                    withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh 'docker build . -t muffius/demo-repo:jma-3.0'
                        sh 'docker push muffius/demo-repo:jma-3.0'
                    }
                }
            }
        }
        
        stage('Deploy to DEV') {
            steps {
                sshagent(['dev-key']) {
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@44.202.39.78 \"${DOCKER_CMD}\""
                }
            }
        }
    }
}
