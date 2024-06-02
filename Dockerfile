FROM eclipse-temurin:17-jdk-alpine
EXPOSE 80:8080
COPY target/akita-backend-service-0.0.1-SNAPSHOT.jar akita-backend-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","akita-backend-service-0.0.1-SNAPSHOT.jar"]