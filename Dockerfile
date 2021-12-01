FROM openjdk:8-jre-slim
COPY ./build/libs/DSM_FindApple_Server_Socket-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p /fcm
ENTRYPOINT ["java", "-jar", "-Xmx1000m", "/app.jar"]
EXPOSE 8081