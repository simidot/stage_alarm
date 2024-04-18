# JDK 가져옴
FROM amazoncorretto:17 AS builder

# 작업할 경로 설정
WORKDIR /app

# 빌드된 JAR 파일을 아티팩트에서 가져옴
COPY ./artifact.jar app.jar

FROM amazoncorretto:17

WORKDIR /app

# 이전 stage에서 복사한 JAR 파일을 현재 stage로 복사
COPY --from=builder /app/app.jar app.jar

ENV PROFILE="dev"

ENTRYPOINT java -jar app.jar --spring.profiles.active=$PROFILE
