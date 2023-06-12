#!/bin/bash
mvn clean install
docker build -f ./Dockerfile --tag=digital-design-task:1 .
