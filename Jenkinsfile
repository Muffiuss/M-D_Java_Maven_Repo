pipeline {
    agent any 

    tools {
        maven 'Maven'
    }
    
    stages {
         
        stage('Build App') {
            steps {
                script {
                  def gv = load 'script.groovy'
                  gv.buildApp()
                }
            }
        }
        
        stage('test') {
            steps {
                script {
                  def gv = load 'script.groovy'
                  gv.testApp()
                }
            }
        }
        
        stage('Build Image') {
            steps {
                script {
                  def gv = load 'script.groovy'
                  gv.buildImage()
                }
            }
        }
        
        stage('Deploy to DEV') {
            steps {
               script {
                 def gv = load 'script.groovy'
                 gv.deployApp()
               }
            }
        }
    }
}
