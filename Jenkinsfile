#!/usr/bin/env groovy


node {
    stage('checkout') {
		git branch: 'master', url: 'https://github.com/pdelaby/demo-spring-boot-admin.git'
    }
	 
	 //stage('checkout'){
	//	checkout scm
	 //}

	// si jenkins est dans un docker, il faut rajouter -u root
    docker.image('openjdk:8').inside('-e MAVEN_OPTS="-Duser.home=./"') {
		stage('check tools') {
             parallel(
                 java: {
                    sh "java -version"
                },
                maven: {
                    sh "chmod +x mvnw"
                    sh "./mvnw -version"
                }
            )
        }
        
        
		stage('clean') {
			sh "./mvnw clean"
		}
        
		stage('backend tests') {
            try {
                sh "./mvnw test"
            } catch(err) {
                throw err
            } finally {
                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }
 
        stage('packaging') {
            sh "./mvnw verify -Pprod -DskipTests"
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
        }
           
       
    }
	def dockerImage
    stage('build docker') {
		def pom = readMavenPom file: 'pom.xml'
		 try {
            sh "cp -R src/main/docker target/"
            sh "cp target/*.jar target/docker/"
            dockerImage = docker.build("fac/spring-boot-admin:${pom.version}", 'target/docker')
        } catch(err) {
                throw err
        } finally {
            sh "docker tag fac/spring-boot-admin:${pom.version} fac/spring-boot-admin:latest"
        }
    }
	
	def isDeployGo = true
	try {
		timeout(time: 30, unit: 'SECONDS') { 
			userInput = input(id: 'doDeploy', message: 'Deployer l\'image ?', ok: 'Deployer')
		}
	} catch(err) { // timeout reached or input false
		def user = err.getCauses()[0].getUser()
		echo "Annulé par: [${user}]"
		isDeployGo = false
	}
		
	stage('start docker'){			
		if(isDeployGo){
			sh "docker-compose -f src/main/docker/app.yml stop"
			sh "docker-compose -f src/main/docker/app.yml up --build -d "
		} else {
			echo "Deployement annulé"
		}
	}
}