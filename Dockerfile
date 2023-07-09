
FROM maven:3.8.1-openjdk-11-slim AS build
WORKDIR /usr/app/
COPY src /usr/app/src
COPY pom.xml /usr/app
RUN mvn clean package

FROM adoptopenjdk:11-jre-hotspot
WORKDIR /usr/app/

COPY --from=build /usr/app/target/java-maven-app-1.1.0-SNAPSHOT.jar /usr/app/

CMD [ "java", "-jar", "java-maven-app-1.1.0-SNAPSHOT.jar" ]
