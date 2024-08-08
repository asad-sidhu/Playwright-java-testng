pipeline {
    agent any

    tools {
        maven 'maven'
    }

    stages {
        stage('Build') {
            steps {
                git 'https://github.com/jglick/simple-maven-project-with-tests.git'
                bat "mvn clean package"
            }
            post {
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage("Deploy to QA") {
            steps {
                echo "deploy to QA"
            }
        }

        stage('Regression Automation Test') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    git 'https://github.com/asad-sidhu/Playwright-java-testng'
                    bat "mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/testNG.xml"
                }
            }
        }

        stage('Publish Extent Report') {
            steps {
                publishHTML([allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'reports',
                    reportTitles: ''])
            }
        }
    }
}
