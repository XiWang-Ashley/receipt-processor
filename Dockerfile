# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Package the application (skip tests to save time)
RUN ./mvnw dependency:go-offline

# Copy the project source code into the container
COPY src ./src

# Package the application
RUN ./mvnw clean package -DskipTests

# Expose port 8080 to the outside world
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "target/receipt-processor-0.0.1-SNAPSHOT.jar"]
