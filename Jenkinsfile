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

        stage('Checkstyle') {
            steps {
                sh './mvnw checkstyle:check'
            }
        }

        stage('Static Analysis - Semgrep') {
            steps {
                sh '''
                    export PATH="$HOME/.local/bin:$PATH"
                    semgrep --version
                    semgrep scan --config p/java .
                    semgrep scan --config p/secrets .
                '''
            }
        }

        stage('Static Analysis - SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh './mvnw sonar:sonar'
                }
            }
        }

        stage('Prepare Reports Dir') {
            steps {
                sh 'mkdir -p reports'
            }
        }

        stage('Start Application') {
            steps {
                sh '''
                    nohup java -jar target/*.jar > app.log 2>&1 &
                    echo $! > app.pid
                    sleep 20
                    curl -sf http://localhost:8080/actuator/health || curl -sf http://localhost:8080/ || curl -sf http://localhost:8080/api/status || echo "App may not be up yet"
                '''
            }
        }

        stage('Dynamic Analysis - OWASP ZAP') {
            steps {
                sh '''
                    if [ -f endpoints.txt ]; then

                        echo "Endpoints file:"
                        cat endpoints.txt

                        while IFS= read -r TARGET_URL || [ -n "$TARGET_URL" ]
                        do
                            echo "Scanning: $TARGET_URL"
                            /opt/zap/zap.sh \
                            -cmd \
                            -port 8090 \
                            -quickurl "$TARGET_URL" \
                            -quickout "$WORKSPACE/reports/zap-$(echo $TARGET_URL | md5sum | cut -d' ' -f1).html" \
                            -quickprogress || true
                        done < endpoints.txt
                    else
                        echo "endpoints.txt not found"
                    fi
                '''
            }
        }

        stage('Dynamic Analysis - Nmap Port Scan') {
            steps {
                sh '''
                    nmap -sV -p 8080 localhost -oN reports/nmap-report.txt
                '''
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
    }

        post {
            always {
                sh '''
                    if [ -f app.pid ]; then
                        kill $(cat app.pid) || true
                        rm -f app.pid
                    fi
                '''
                echo 'Archive Reports'
                archiveArtifacts artifacts: 'reports/**/*', allowEmptyArchive: true
            }
        }

}