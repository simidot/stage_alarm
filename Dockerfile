# JDK 가져옴
FROM amazoncorretto:17 AS builder

# 작업할 경로 설정
WORKDIR /app
# COPY로 이미지내부에 복사해 올 외부의 파일 및 폴더 명시
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src/main ./src/main
RUN sed -i 's/\r//' ./gradlew && ./gradlew bootJar

FROM amazoncorretto:17

WORKDIR /app
COPY --from=builder /app/build/libs/stage-alarm-*.jar app.jar

ENV PROFILE="dev"

ENTRYPOINT java -jar app.jar --spring.profiles.active=$PROFILE