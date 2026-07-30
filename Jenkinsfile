pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Static Security Checks') {

            steps {
                echo 'Running static security checks'
            }

        }

//         stage('Checkstyle') {
//             steps {
//                 sh './mvnw checkstyle:check'
//             }
//         }

        stage('Semgrep') {
            steps {
                sh '''
                    export PATH="$HOME/.local/bin:$PATH"
                    semgrep --version
                    semgrep scan --config p/java .
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh './mvnw sonar:sonar'
                }
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
    }
}