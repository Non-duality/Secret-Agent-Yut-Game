FROM openjdk:17-alpine
EXPOSE 8081

# OpenVidu 설정을 추가합니다.
FROM openvidu/openvidu-dev:2.29.0
EXPOSE 4443
CMD ["-e", "OPENVIDU_SECRET=MY_SECRET"]

# 기존의 Spring Boot 애플리케이션 설정을 계속 진행합니다.
ARG JAR_FILE=BE-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
