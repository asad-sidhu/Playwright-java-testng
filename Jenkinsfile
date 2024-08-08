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
            }
            archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
        }

        stage("Deploy to QA") {
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

        stage('Publish Extent Report') { // Renamed and separate stage for clarity
            steps {
                script {
                    // Assuming Extent Report is generated in 'reports' directory
                    def reportFiles = fileGlob(dir: 'reports', files: 'TestExecutionReport.html')
                    if (reportFiles.any()) {
                        publishHTML([
                            allowMissing: false,
                            alwaysLinkToLastBuild: false,
                            keepAll: true,
                            reportDir: 'reports',
                            reportFiles: reportFiles,
                            reportName: 'HTML Extent Report',
                            reportTitles: ''
                        ])
                    } else {
                        echo "Extent Report not found. Might be due to test failures."
                    }
                }
            }
        }
    }
}
