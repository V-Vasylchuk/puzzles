FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} puzzles.jar
ENTRYPOINT ["java","-jar","/puzzles.jar"]