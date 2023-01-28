pipeline {
    agent any
    environment {
        AZURE_CLIENT_ID = "bbea970d-df9d-4967-ab08-268a8bd37c92"
        AZURE_CLIENT_SECRET = "nre8Q~zb7eYYme2aeZ3YJDKViADOJK6zsyuWUbul"
        AZURE_TENANT_ID = "25ac4c27-5513-4bea-828e-cce60779b2a8"
        AZURE_SUBSCRIPTION_ID = "a8dd044b-a140-40b4-855d-66dbcdc99f31"
        AKS_CLUSTER_NAME = "my-aks-cluster"
        AKS_RESOURCE_GROUP = "my-aks-rg"
        AKS_NAMESPACE = "my-aks-ns"
    }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/Azure-Samples/azure-voting-app-redis.git'
            }
        }
        stage('Deploy to AKS') {
            steps {
                withCredentials([string(credentialsId: 'AZURE_CLIENT_ID', variable: 'AZURE_CLIENT_ID'),
                                 string(credentialsId: 'AZURE_CLIENT_SECRET', variable: 'AZURE_CLIENT_SECRET'),
                                 string(credentialsId: 'AZURE_TENANT_ID', variable: 'AZURE_TENANT_ID'),
                                 string(credentialsId: 'AZURE_SUBSCRIPTION_ID', variable: 'AZURE_SUBSCRIPTION_ID')]) {
                    sh '''
                        az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET --tenant $AZURE_TENANT_ID
                        az aks get-credentials --resource-group $AKS_RESOURCE_GROUP --name $AKS_CLUSTER_NAME --overwrite-existing --admin
                        kubectl create namespace $AKS_NAMESPACE
                        kubectl apply -f azure-vote.yaml -n $AKS_NAMESPACE
                    '''
                }
            }
        }
    }
}
