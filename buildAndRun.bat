@echo off
call mvn clean package
call docker build -t ci.gouv.dgbf/mic-acte-gestion-credit .
call docker rm -f mic-acte-gestion-credit
call docker run -d -p 9080:9080 -p 9443:9443 --name mic-acte-gestion-credit ci.gouv.dgbf/mic-acte-gestion-credit