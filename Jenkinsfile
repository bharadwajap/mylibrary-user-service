#!groovy
    def javaAgent = 'docker-slave'

    def branch
	def projectName = 'mylibrary-user-service'
	def gitCredentials = 'mylibrary-github'
    // pipeline
    node(javaAgent) {
    	properties([
            [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator',daysToKeepStr: '1', numToKeepStr: '4']],
            parameters([
                	
			string(
				defaultValue: 'localhost',
				description: 'Config server host ip',
				name: 'configServerIp'
			)
            ])
         ])
                     
        try {
            stage('Collect info') {
                checkout scm
                branch = env.BRANCH_NAME
            }

            stage('Build') {
				sh "mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.skip=true"
                stash 'workspace'
            }


			stage('Dockerize') {
				unstash 'workspace'
            	final String activeContainers = sh(script: "sudo docker ps -a", returnStdout: true)
	            boolean containerFound = activeContainers.toLowerCase().contains("${projectName}")
	            if (containerFound) {
	                sh "sudo docker stop ${projectName}"
	                sh "sudo docker rm ${projectName}"
	            }
	            sh "sudo docker build -t ${projectName} ."
				sh "sudo docker run --restart always --network=host --name=${projectName} -v /opt/mylibrary:/opt/app/mylibrary-h2-db:z -e CONFIG_SERVER_URI=http://${params.configServerIp}:8888 -td ${projectName}"
			}
        } catch (def e) {
			print "Exception occurred while running the pipeline"+ e
        } finally {
        	deleteDir()
    	}
    }