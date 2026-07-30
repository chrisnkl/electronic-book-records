pipeline {
    agent any

    tools {
        maven 'maven3.9.16'
        jdk 'jdk21'
    }

    environment {
        MAVEN_HOME = tool 'maven3.9.16'
        JAVA_HOME = tool 'jdk21'
        PATH = "${MAVEN_HOME}/bin:${JAVA_HOME}/bin:${env.PATH}"
    }

    stages {

        stage('Debug tools') {
            steps {
                sh '''
                    echo $JAVA_HOME
                    echo $MAVEN_HOME
                    java -version
                    mvn -version
                '''
            }
        }

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

        stage('Static Security Checks') {

            steps {
                echo 'Running static security checks'
            }

        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }
}