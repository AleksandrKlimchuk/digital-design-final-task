#!/bin/bash
mvn clean install
sudo docker build -f ./Dockerfile --tag=digital-design-task:1 .