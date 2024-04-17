## 도커파일 내에서 빌드하고 빌드된 jar파일을 지정해서 이미지 내부로 복사해오는 도커파일
#FROM amazoncorretto:17 AS builder
#
## 작업할 경로 설정
#WORKDIR /app
## COPY로 이미지내부에 복사해 올 외부의 파일 및 폴더 명시
#COPY gradlew build.gradle settings.gradle ./
#COPY gradle ./gradle
#COPY src/main ./src/main
#RUN sed -i 's/\r//' ./gradlew && ./gradlew bootJar
#
#FROM amazoncorretto:17
#
#WORKDIR /app
#COPY --from=builder /app/build/libs/stage-alarm-*.jar app.jar
#
#ENV PROFILE="dev"
#
#ENTRYPOINT java -jar app.jar --spring.profiles.active=$PROFILE

# JDK 가져옴
FROM amazoncorretto:17 AS builder

# 작업할 경로 설정
WORKDIR /app

# 빌드된 JAR 파일을 아티팩트에서 가져옴
COPY path/to/artifact.jar app.jar

FROM amazoncorretto:17

WORKDIR /app

# 이전 stage에서 복사한 JAR 파일을 현재 stage로 복사
COPY --from=builder /app/app.jar app.jar

ENV PROFILE="dev"

ENTRYPOINT java -jar app.jar --spring.profiles.active=$PROFILE

