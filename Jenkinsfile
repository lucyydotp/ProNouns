pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean versions:set -DnewVersion=${git describe --tags} package'
                archiveArtifacts artifacts: 'pronouns-bukkit/target/*.jar'
            }
        }
    }
}