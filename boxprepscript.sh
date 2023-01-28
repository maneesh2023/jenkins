#!/bin/bash

sudo apt-get update
sudo apt install -y git net-tools docker.io
sudo snap install docker
sudo rm -rf ~/github
sudo mkdir ~/github
cd ~/github
sudo git clone https://github.com/maneesh2023/jenkins.git
cd ~/github/jenkins
sudo docker-compose down
sudo docker-compose pull
sudo docker-compose up
