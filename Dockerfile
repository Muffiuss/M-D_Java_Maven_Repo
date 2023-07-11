FROM maven:3.8.1-openjdk-11-slim AS build

WORKDIR /usr/app/       

COPY pom.xml /usr/app/
RUN mvn dependency:go-offline --no-transfer-progress

COPY src /usr/app/src
RUN mvn clean package --no-transfer-progress

FROM adoptopenjdk:11-jre-hotspot-bionic

WORKDIR /usr/app/ 

RUN addgroup --system app && adduser --system --group app
USER app

COPY --from=build /usr/app/target/java-maven-app-*.jar /usr/app/

EXPOSE 8080

CMD  java -XX:+UseContainerSupport -XX:MaxRAMPercentage=50.0 -jar java-maven-*.jar 
