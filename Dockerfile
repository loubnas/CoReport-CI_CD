FROM openjdk:17-alpine
COPY target/identity.jar identity.jar
ENTRYPOINT ["java","-jar","/identity.jar"]