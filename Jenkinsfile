pipeline {
    agent any
    tools {
        maven 'maven3'
        jdk 'jdk8'
        nodejs 'nodesjs10.23'
    }
    stages {
        stage('checkout') {
            steps {
                git credentialsId: 'git212', url: 'http://admin@114.215.129.212:23001/r/wms3.1.git'
            }
        }
        stage('buildWmsUI') {
            steps {
                dir('SourceCode/WmsUI') {
                    sh 'npm config set registry https://registry.npm.taobao.org'
                    sh 'npm config get registry'
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }
        stage('buildWmsPda') {
            steps {
                dir('SourceCode/WmsPda') {
                    sh 'npm config set registry https://registry.npm.taobao.org'
                    sh 'npm config get registry'
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }
        stage('buildService') {
            steps {
                dir('SourceCode/nodes-tool') {
                    sh 'mvn clean install package -Dmaven.test.skip=true'
                }
            }
        }

        stage('DeployMasterTo162') {
            steps {
                dir('SourceCode') {
                    sh 'scp -r WmsUI/dist/* administrator@192.168.1.162:./WMS/nginx-1.18.0/html/testui/'
                    sh 'scp -r WmsPda/www/* administrator@192.168.1.162:./WMS/nginx-1.18.0/html/testpda/'
                    warnError(catchInterruptions:false, message:'stop service fail'){
                        sh 'ssh administrator@192.168.1.162 \"nssm stop wms-test\"'
                    }
                    sh 'scp -r nodes-tool/nodes-wms/target/NodeX.jar administrator@192.168.1.162:./WMS/WMS_test/NodeX.jar'
                }
            }
        }
        stage('RunService') {
            steps{
                sh 'ssh administrator@192.168.1.162 \"start C:/Users/Administrator/WMS/nginx-1.18.0/nginx-reload.bat\"'
                warnError(catchInterruptions:false, message:'start service fail'){
                    sh 'ssh administrator@192.168.1.162 \"nssm start wms-test"'
                }
            }
        }
    }
}
