FROM openjdk:8-jre-alpine
COPY target/osc-world-*-SNAPSHOT-jar-with-dependencies.jar /app.jar
EXPOSE 40000
CMD /usr/bin/java -jar /app.jar