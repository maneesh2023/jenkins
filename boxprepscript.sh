#!/bin/bash

sudo apt-get update
sudo apt install -y git net-tools docker.io
sudo snap install docker
curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash
sudo mkdir ~/myprojects
cd ~/myprojects
sudo git clone https://github.com/maneesh2023/jenkins.git
cd ~/myprojects/jenkins
sudo docker-compose pull
sudo docker-compose up
