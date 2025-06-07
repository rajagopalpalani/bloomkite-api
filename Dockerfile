FROM tomcat:8

COPY target/*.war /usr/local/tomcat/webapps/app.war

RUN chmod 777 /usr/local/tomcat/webapps/app.war

EXPOSE 8080