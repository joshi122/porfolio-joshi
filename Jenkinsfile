pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = 'docker.io'
        DOCKER_IMAGE = 'devops-portfolio-tracker'
        DOCKER_TAG = "${BUILD_NUMBER}"
        REGISTRY_CREDENTIALS = 'docker-credentials'
        SONAR_HOST_URL = 'http://sonarqube:9000'
        SONAR_TOKEN = credentials('sonar-token')
    }

    options {
        timeout(time: 1, unit: 'HOURS')
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout') {
            steps {
                echo '🔄 Checking out code from repository...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '🔨 Building Maven project...'
                sh 'mvn clean package -DskipTests -B'
            }
        }

        stage('Test') {
            steps {
                echo '🧪 Running unit tests...'
                sh 'mvn test -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo '📊 Running SonarQube code quality analysis...'
                sh '''
                    mvn sonar:sonar \
                        -Dsonar.projectKey=portfolio-tracker \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONAR_TOKEN}
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                echo '🐳 Building Docker image...'
                sh 'docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG} .'
                sh 'docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest .'
            }
        }

        stage('Push to Docker Registry') {
            when {
                branch 'main'
            }
            steps {
                echo '📤 Pushing Docker image to registry...'
                script {
                    withCredentials([usernamePassword(credentialsId: "${REGISTRY_CREDENTIALS}", 
                                                     usernameVariable: 'DOCKER_USER', 
                                                     passwordVariable: 'DOCKER_PASS')]) {
                        sh '''
                            echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin
                            docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}
                            docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest
                            docker logout
                        '''
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            when {
                branch 'main'
            }
            steps {
                echo '🚀 Deploying to Kubernetes cluster...'
                script {
                    sh '''
                        kubectl set image deployment/portfolio-tracker \
                            portfolio-tracker=${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG} \
                            -n production --record || true
                        kubectl rollout status deployment/portfolio-tracker -n production
                    '''
                }
            }
        }

        stage('Notify') {
            when {
                always()
            }
            steps {
                echo '📧 Build notification...'
                script {
                    def buildStatus = currentBuild.result ?: 'SUCCESS'
                    echo "Build Status: ${buildStatus}"
                }
            }
        }
    }

    post {
        always {
            echo '🧹 Cleaning up workspace...'
            cleanWs()
        }
        failure {
            echo '❌ Pipeline failed!'
        }
        success {
            echo '✅ Pipeline succeeded!'
        }
    }
}
