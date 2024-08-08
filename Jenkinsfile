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
            post {
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
                }
            }
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
        
        stage('Publish Extent Report') {
            steps {
                publishHTML([allowMissing: false,
                             alwaysLinkToLastBuild: false, 
                             keepAll: true, 
                             reportDir: 'reports', 
                             reportFiles: 'TestExecutionReport.html', 
                             reportName: 'HTML Extent Report', 
                             reportTitles: ''])
            }
        }
    }
}
