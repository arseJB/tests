FROM maven:3.8.1-openjdk-11-slim AS MAVEN_BUILD
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn clean install 

FROM openjdk:11-jre-slim
LABEL authors="arseniojb"
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/rmclient-1.0.jar /app/
CMD ["java", "-jar", "rmclient-1.0.jar"]
