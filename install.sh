#!/usr/bin/env bash

echo "Giving permissions for directories"
sudo chmod a+w media

echo "Build application"
mvn -D skipTests=true package docker:build

echo "Run docker-compose containers"
docker-compose up -d
