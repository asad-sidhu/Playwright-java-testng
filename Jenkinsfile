pipeline 
{
    agent any
    
    tools{
    	maven 'maven'
        }

    stages 
    {
        stage('Build') 
        {
            steps
            {
                git url: 'https://github.com/asad-sidhu/randomtestjenkins.git', branch: 'main'
                bat 'mvn clean package'
            }
            post 
            {
                success
                {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
        
        
        
        stage("Deploy to QA"){
            steps{
                echo("deploy to qa")
            }
        }
                
        stage('Regression Automation Test') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    git url: 'https://github.com/asad-sidhu/Playwright-java-testng.git', branch: 'main'
                    bat 'mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/testNG.xml'
         
                }
            }
        }
        
        
        stage('Publish Extent Report'){
            steps{
                     publishHTML([allowMissing: false,
                                  alwaysLinkToLastBuild: false, 
                                  keepAll: true, 
                                  reportDir: 'Playwright-java-testng\\reports', 
                                  reportFiles: '*.html', 
                                  reportName: 'HTML Extent Report', 
                                  reportTitles: ''])
            }
        }
        
        
        
        
    }
}
