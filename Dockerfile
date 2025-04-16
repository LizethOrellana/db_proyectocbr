# First stage: Maven image to build the Spring Boot application
FROM maven:3.8.4-openjdk-17 as build-stage
WORKDIR /app
# Copy the project files into the image
COPY pom.xml .
COPY src ./src
# Build the application, skipping tests
RUN mvn clean package -DskipTests

# Second stage: OpenJDK image to run the application
FROM openjdk:17-jdk-alpine
WORKDIR /backend
# Copy the JAR file from the build stage
COPY --from=build-stage /app/target/db_proyectocbr-0.0.1-SNAPSHOT.jar /backend/db_proyectocbr-0.0.1.jar
ENTRYPOINT ["java", "-jar", "db_proyectocbr-0.0.1.jar"]