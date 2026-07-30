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

//         stage('Semgrep') {
//             steps {
//                 sh '''
//                     export PATH="$HOME/.local/bin:$PATH"
//                     semgrep --version
//                     semgrep scan --config p/java . --error
//                     semgrep scan --config p/secrets . --error
//                     --error
//                 '''
//             }
//         }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh './mvnw sonar:sonar'
                }
            }
        }

        stage('Dynamic Security Checks') {

            steps {
                echo 'Running dynamic security checks'
            }

        }

        stage('Start Application') {
            steps {
                sh '''
                    nohup java -jar target/*.jar > app.log 2>&1 &
                    sleep 20
                '''
            }
        }

        stage('Nmap Port Scan') {
            steps {
                sh '''
                    nmap -p 8080 localhost
                '''
            }
        }

        stage('Running tests') {
            steps {
                sh '''
                    nohup java -jar target/*.jar > app.log 2>&1 &
                    sleep 20
                '''
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
    }
}