pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'mkdir -p reports'
            }
        }

        stage('Static Analysis - Semgrep') {
            steps {
                sh '''
                    docker run --rm \
                    -v "${WORKSPACE}:/src" \
                    -w /src \
                    semgrep/semgrep \
                    semgrep scan --config p/java --config p/secrets --json --output /src/reports/semgrep-report.json || true
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube Quality & Security Scan'
                withSonarQubeEnv('sonarqube') {
                    sh './mvnw sonar:sonar -Dsonar.projectKey=ebr-app -Dsonar.projectName=ebr-app'
                }
            }
        }

        stage('Build & Containerize') {
            steps {
                sh './mvnw clean package -DskipTests'
                sh 'docker build -t ebr-app:latest .'
            }
        }

        stage('Image Security Scan') {
            steps {
                sh 'trivy image --format json -o reports/trivy-image-report.json ebr-app:latest || true'
            }
        }

        stage('Starting the application') {
            steps {
                sh '''
                    docker run -d --name ebr-app-test -p 8080:8080 --network ebr_default ebr-app:latest || true
                    sleep 20
                '''
            }
        }

        stage('Dynamic Analysis - Nmap Port Scan') {
            steps {
                echo '=== Running Nmap Port & Service Scan ==='
                sh 'nmap -p 8080,5432,9000 -sV localhost -oX reports/nmap-report.xml || true'
            }
        }

        stage('Dynamic Analysis - SQLMap Injection Test') {
            steps {
                echo '=== Running SQLMap against defined endpoints ==='
                sh '''
                    TARGET_URL=$(head -n 1 endpoints.txt)
                    sqlmap -u "$TARGET_URL" --batch --risk=1 --level=1 --dump-format=HTML --output-dir=reports/sqlmap/ || true
                '''
            }
        }

        stage('Cleanup & Stop App') {
            steps {
                echo 'Cleaning up DAST container'
                sh '''
                    docker stop ebr-app-test || true
                    docker rm ebr-app-test || true
                '''
            }
        }

        stage('Unit Tests') {
            steps {
                sh './mvnw test || true'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'reports/**/*', allowEmptyArchive: true
            echo 'Reports have been generated in ./reports folder'
        }
    }
}