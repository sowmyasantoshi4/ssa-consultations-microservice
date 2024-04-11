FROM openjdk:17-alpine

VOLUME /tmp

ARG JAR_FILE=./target/*.jar

COPY ${JAR_FILE} consultation.jar

EXPOSE 8083

ENTRYPOINT ["java","-jar","/consultation.jar"]