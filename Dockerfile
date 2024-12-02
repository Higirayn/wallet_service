FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/*.jar wallet-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/wallet-service.jar"]