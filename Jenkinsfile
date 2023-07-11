pipeline {
    agent any 

    tools {
        maven 'Maven'
    }

    environment {
       groovy = load 'script.groovy'
    }
    
    stages {
         
        stage('Build App') {
            steps {
                script {
                  groovy.buildApp()
                }
            }
        }
        
        stage('test') {
            steps {
                script {
                   groovy.testApp()
                }
            }
        }
        
        stage('Build Image') {
            steps {
                script {
                    groovy.buildImage()
                }
            }
        }
        
        stage('Deploy to DEV') {
            steps {
               script {
                  groovy.deployApp()
               }
            }
        }
    }
}
