pipeline {
    agent any 

    tools {
        maven 'Maven'
    }
    
    stages {
        stage('Load script') {
            steps {
                script {
                    gv = load 'script.groovy'
                }
            }
        }
         
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
