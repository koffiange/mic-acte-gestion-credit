#!/bin/sh
if [ $(docker ps -a -f name=mic-acte-gestion-credit | grep -w mic-acte-gestion-credit | wc -l) -eq 1 ]; then
  docker rm -f mic-acte-gestion-credit
fi
mvn clean package && docker build -t ci.gouv.dgbf/mic-acte-gestion-credit .
docker run -d -p 9080:9080 -p 9443:9443 --name mic-acte-gestion-credit ci.gouv.dgbf/mic-acte-gestion-credit
