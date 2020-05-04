#!groovy
    def javaAgent = 'master'

    def branch
	def projectName = 'mylibrary-user-service'
	def gitCredentials = 'mylibrary-github'
    // pipeline
    node(javaAgent) {

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
            	final String activeContainers = sh(script: "sudo docker ps", returnStdout: true)
	            boolean containerFound = activeContainers.toLowerCase().contains("${projectName}")
	            if (containerFound) {
	                sh "sudo docker --config=\"${WORKSPACE}\" stop ${projectName}"
	                sh "sudo docker --config=\"${WORKSPACE}\" rm ${projectName}"
	            }
	            sh "sudo docker build -t ${projectName} ."
	            
		        withCredentials([usernamePassword(credentialsId: gitCredentials, usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
					sh "sudo docker run --restart always --name=${projectName} -e CONFIG_REPO_USER=${env.GIT_USER} -e CONFIG_REPO_PWD=${env.GIT_PASS} -td ${projectName}"
				}
			}
        } catch (def e) {
			print "Exception occurred while running the pipeline"+ e
        } finally {
        	deleteDir()
    	}
    }