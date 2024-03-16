FROM  maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/json-verifier.jar ./json-verifier.jar
EXPOSE 8080
CMD ["java", "-jar", "json-verifier.jar"]
