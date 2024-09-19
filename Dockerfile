FROM maven:3.9.4-amazoncorretto-17-al2023 as build
COPY /src /app/src
COPY /pom.xml /app
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip

FROM openjdk:17-jdk-alpine
EXPOSE 10000
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]