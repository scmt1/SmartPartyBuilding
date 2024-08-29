FROM openjdk:11.0.8-jdk

MAINTAINER hxh

WORKDIR /home/admin

COPY ./scaffold-server/target/*.jar .
COPY ./docker/start.sh .
EXPOSE 8081
ENV TZ=Asia/Shanghai
RUN /bin/cp /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ  >/etc/timezone

RUN sed -i 's/\r//' ./start.sh
ENTRYPOINT [ "/bin/sh", "/home/admin/start.sh", "start" ]
