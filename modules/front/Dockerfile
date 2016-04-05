FROM java:8-jre

ADD libjpeg-turbo-official_1.4.2_amd64.deb lib.deb

RUN dpkg -i lib.deb

RUN usermod -u 1000 daemon

USER daemon

ADD filecessor-front.jar app.jar

ENV LD_LIBRARY_PATH=/opt/libjpeg-turbo/lib64/

VOLUME ["/media", "/var/tmp/uploads"]

ENTRYPOINT ["java", "-Xmx1g", "-jar","/app.jar"]

EXPOSE 8080
