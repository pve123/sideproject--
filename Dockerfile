FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} side_project.jar
ENTRYPOINT ["java","-jar","/side_project.jar"]