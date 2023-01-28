
    
    
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
                git branch: 'main', url: 'https://github.com/maneesh2023/azure-voting-app-redis.git'
            }
        }
        stage('Build') {
            steps {
                    sh '''
                        docker-compose build
                        docker-compose push
                    '''
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
                        curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
                        chmod +x kubectl
                        mv kubectl /usr/local/bin/
                        curl -L https://aka.ms/InstallAzureCLIDeb | bash
                        az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET --tenant $AZURE_TENANT_ID
                        az aks get-credentials --resource-group $AKS_RESOURCE_GROUP --name $AKS_CLUSTER_NAME --overwrite-existing --admin
                        kubectl get namespace $AKS_NAMESPACE || kubectl create namespace $AKS_NAMESPACE
                        kubectl apply -f azure-vote-all-in-one-redis.yaml -n $AKS_NAMESPACE
                        kubectl get service azure-vote-front -n my-aks-ns -o jsonpath='{.status.loadBalancer.ingress[0].ip}'
                    '''
            }
        }
    }
}

    

    

    
