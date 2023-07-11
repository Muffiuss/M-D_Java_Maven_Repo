def gv

pipeline {
    agent any 

    tools {
        maven 'Maven'
    }
    
    stages {
        stage('Load script') {
            steps {
                script {
                    gv = load "sh.groovy"
                }
            }
        }
         
        stage('Build App') {
            steps {
                script {
               gv.buildJar()
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
               gv.runTest()
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
               gv.devDeploy() 
               }
            }
        }
                stage('clean workspace') {
          steps {
               cleanWs()
          }
        }
    }
}
