pipeline {
    agent any

    tools {
        jdk 'Java21'           // Make sure "Java 21" is configured under Jenkins -> Global Tool Configuration
        gradle 'Gradle 8.5'     // Also ensure "Gradle 8.5" (or your version) is configured
    }

    environment {
        GRADLE_OPTS = '-Dorg.gradle.daemon=false'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                git branch: 'main', url: 'https://github.com/SanduniWickramasinghe/test-generator.git'
            }
        }

        stage('Build') {
            steps {
                echo 'Running Gradle build...'
                sh './gradlew clean build -x test'
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                sh './gradlew test'
            }
            post {
                always {
                    junit '**/build/test-results/test/*.xml'  // Publishes test reports in Jenkins UI
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging application...'
                sh './gradlew bootJar' // For Spring Boot projects; use 'jar' if not Spring Boot
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
                }
            }
        }
    }

    post {
        success {
            echo '✅ Build and test pipeline completed successfully!'
        }
        failure {
            echo '❌ Build or tests failed. Please check logs.'
        }
    }
}
