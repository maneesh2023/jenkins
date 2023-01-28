#!/bin/bash

sudo apt-get update
sudo apt install -y git net-tools docker.io
sudo snap install docker
sudo rm -rf ~/myprojects
sudo mkdir ~/myprojects
cd ~/myprojects
sudo git clone https://github.com/maneesh2023/jenkins.git
cd ~/myprojects/jenkins
sudo docker-compose down
sudo docker-compose pull
sudo docker-compose up
