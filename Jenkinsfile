pipeline {

    agent any

    tools {
        jdk 'jdk21'
        maven 'maven3.9.16'
    }

    stages {

        stage('Checkout') {

            steps {

                checkout scm

            }

        }

        stage('Build') {

            steps {

                sh 'mvn clean package -DskipTests'

            }

        }

        stage('Test') {

            steps {

                sh 'mvn test'

            }

        }


    }

}