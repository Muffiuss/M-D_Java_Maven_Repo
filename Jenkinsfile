gv = null 
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
                  gv.buildApp()
                }
            }
        }
        
        stage('test') {
            steps {
                script {
                   gv.testApp()
                }
            }
        }
        
        stage('Build Image') {
            steps {
                script {
                   gv.buildImage()
                }
            }
        }
        
        stage('Deploy to DEV') {
            steps {
               script {
                 gv.deployApp()
               }
            }
        }
    }
}
