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
//
        stage('Semgrep') {
            steps {
                sh '''
                    export PATH="$HOME/.local/bin:$PATH"
                    semgrep --version
                    semgrep scan --config p/java . --error
                    semgrep scan --config p/secrets . --error
                '''
            }
        }
//
//         stage('SonarQube Analysis') {
//             steps {
//                 withSonarQubeEnv('sonarqube') {
//                     sh './mvnw sonar:sonar'
//                 }
//             }
//         }

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

        stage('Dynamic Security Checks - OWASP ZAP') {
            steps {
                sh '''
                    if [ -f endpoints.txt ]; then
                        TARGET_URL=$(head -n 1 endpoints.txt)
                        echo "Found URLs from endpoints.txt: $TARGET_URL"
                        zap-cli quick-scan --self-contained -l Medium "$TARGET_URL" -r reports/zap-report.html || true
                    else
                        echo "endpoints.txt was not found, therefore skipping ZAP scan."
                    fi
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

        post {
                always {
                    echo 'Archive Reports'
                    archiveArtifacts artifacts: 'reports/**/*', allowEmptyArchive: true
                }
            }

    }
}