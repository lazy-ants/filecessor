FROM java:8-jre

RUN usermod -u 1000 daemon

USER daemon

ADD filecessor-front.jar app.jar

VOLUME ["/media", "/var/tmp/uploads"]

ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8080
