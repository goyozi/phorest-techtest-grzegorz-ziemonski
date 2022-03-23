FROM openjdk:17

COPY build/libs/*-all.jar app.jar

CMD java -jar app.jar
