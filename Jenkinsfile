def gv


pipeline {
    agent any 

    tools {
        maven 'Maven'
    }

    environment {
     DOCKER_REPO = 'muffius/demo-repo'
    }
    
    stages {
        stage('Load script') {
            steps {
                script {
                    gv = load "sh.groovy"
                }
            }
        }
        stage('version Increment') {
          steps {
              script {
                gv.incrementVersion()
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
        stage('commit version') {
          steps {
          script {
                gv.commitVersion()
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
