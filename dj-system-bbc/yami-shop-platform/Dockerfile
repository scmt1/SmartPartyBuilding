FROM openjdk:8u322-jre-buster as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:8u322-jre-buster
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-Xms1024m", "-Xmx1024m", "-Xss256k", "-XX:SurvivorRatio=8", "-Dspring.profiles.active=docker", "org.springframework.boot.loader.JarLauncher"]