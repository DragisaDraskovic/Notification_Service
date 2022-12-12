FROM maven:3.8.5-openjdk-17-slim AS notificationBackendBuild
WORKDIR /root/src/backend
COPY . .
RUN mvn package  -DskipTests

FROM openjdk:17 AS notificationBackendRuntime
WORKDIR /app
COPY --from=notificationBackendBuild /root/src/backend/target/notification-exe.jar ./
EXPOSE 8083
CMD java -jar notification-exe.jar
