pipeline {
    agent any 

    tools {
        maven 'Maven'
    }
    
    stages {
        stage('Load script') {
            steps {
                script {
                    env.gv = load 'script.groovy'
                }
            }
        }
         
        stage('Build App') {
            steps {
                script {
                  env.gv.buildApp()
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                   env.gv.testApp()
                }
            }
        }
        
        stage('Build Image') {
            steps {
                script {
                   env.gv.buildImage()
                }
            }
        }
        
        stage('Deploy to DEV') {
            steps {
               script {
                 env.gv.deployApp()
               }
            }
        }
    }
}

