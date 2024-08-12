pipeline {
    agent any

    tools {
        maven 'maven'
    }

    stages {
        stage('Build') {
            steps {
                git url: 'https://github.com/asad-sidhu/randomtestjenkins.git', branch: 'main'
                bat 'mvn clean package'
                archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
            }
        }

        stage('Deploy to QA') {
            steps {
                echo "Deploying to QA"
            }
        }

        stage('Regression Automation Test') {
            steps {
                dir('Playwright-java-testng') { // Navigate into the correct directory
                    git url: 'https://github.com/asad-sidhu/Playwright-java-testng.git', branch: 'main'
                    bat 'mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/testNG.xml'
                }
            }
        }
    }

    post {
        always {
            script {
                def reportDir = 'Playwright-java-testng\\reports'

                // Check if any HTML report exists in the directory
                def reportExists = bat(script: "dir /b ${reportDir}\\*.html", returnStatus: true) == 0

                if (reportExists) {
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: reportDir,
                        reportFiles: '*.html', // Include all HTML files
                        reportName: 'HTML Extent Reports',
                        reportTitles: ''
                    ])
                } else {
                    echo "No Extent Reports found. Might be due to test failures."
                }
            }
        }
    }
}
