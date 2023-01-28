
    pipeline {
    agent any
    environment {
        AZURE_CLIENT_ID = "5077da19-23ba-4ff7-b6db-689c3ea6b6c5"
        AZURE_CLIENT_SECRET = "qHV8Q~hgPnd7gIXfS2iiEdN.lwBvENNu_wpvsdbk"
        AZURE_TENANT_ID = "257372eb-856d-4472-9ba7-d63a8ae6f63b"
        AZURE_SUBSCRIPTION_ID = "a78e6e3e-c9fe-4863-be70-f602072c40e2"
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
         stage('AKS Resource Check') {
            steps {
                    sh '''
                       # curl -L https://aka.ms/InstallAzureCLIDeb | bash
                       # az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET --tenant $AZURE_TENANT_ID
                       # az aks create --resource-group my-aks-rg --name my-aks-cluster --generate-ssh-keys
                    '''
            }
        }
        stage('Deploy to AKS') {
            steps {
                    sh '''
                        curl -L https://aka.ms/InstallAzureCLIDeb | bash
                        az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET --tenant $AZURE_TENANT_ID
                        az aks get-credentials --resource-group $AKS_RESOURCE_GROUP --name $AKS_CLUSTER_NAME --overwrite-existing --admin
                        kubectl create namespace $AKS_NAMESPACE
                        kubectl apply -f azure-vote.yaml -n $AKS_NAMESPACE
                    '''
            }
        }
    }
}

    
