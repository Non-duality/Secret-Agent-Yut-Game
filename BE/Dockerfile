FROM openjdk:17-alpine
EXPOSE 5000

# 기존의 Spring Boot 애플리케이션 설정을 계속 진행합니다.
ARG JAR_FILE=BE-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
