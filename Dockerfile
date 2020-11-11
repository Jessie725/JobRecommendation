FROM tomcat:9.0.36-jdk14-openjdk-oracle
MAINTAINER jessienyu725@gmail.com

ADD ./jupiter.war /usr/local/tomcat/webapps
EXPOSE 8080
CMD ["catalina.sh", "run"]

