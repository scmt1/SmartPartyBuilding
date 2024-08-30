FROM openjdk:11.0.8-jdk

MAINTAINER hxh

WORKDIR /home/admin

COPY ./target/*.jar .
COPY ./docker/start.sh .
EXPOSE 7887

RUN sed -i 's/\r//' ./start.sh
ENTRYPOINT [ "/bin/sh", "/home/admin/start.sh", "start" ]

