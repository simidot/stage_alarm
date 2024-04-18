# 첫 번째 단계: JDK를 가져와서 builder stage를 만듭니다.
FROM amazoncorretto:17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일을 복사합니다. 해당 경로와 파일 이름은 CI/CD 파이프라인 설정에 따라 변경될 수 있습니다.
COPY ./build/libs/*-SNAPSHOT.jar app.jar

# 두 번째 단계: 빌더 stage에서 만들어진 JAR 파일을 가져와서 실행 환경을 구성합니다.
FROM amazoncorretto:17

# 작업 디렉토리 설정
WORKDIR /app

# 이전 stage에서 복사한 JAR 파일을 현재 stage로 복사합니다.
COPY --from=builder /app/app.jar app.jar

# 환경 변수 설정 (프로파일은 "dev"로 설정됩니다. 필요에 따라 변경 가능합니다.)
ENV PROFILE="dev"

# 애플리케이션 실행 명령
ENTRYPOINT java -jar app.jar --spring.profiles.active=$PROFILE
