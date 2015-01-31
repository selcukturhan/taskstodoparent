#!/bin/bash
reset
sudo service mysql start
mvn install clean
export MAVEN_OPTS="-Xmx512M -XX:MaxPermSize=512m"
export MAVEN_OPTS="-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n"
mvn tomcat:run
