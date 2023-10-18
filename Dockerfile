FROM openjdk:11-jre-slim
LABEL authors="arseniojb"
WORKDIR /app
COPY target/rmclient-1.0.jar .
CMD ["java", "-jar", "rmclient-1.0.jar"]
