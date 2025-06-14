# 1. 빌드 컨텍스트 최적화
FROM maven:3.9.9-amazoncorretto-21-debian-bookworm AS builder
WORKDIR /app

# gradle wrapper만 먼저 복사
COPY gradle gradle
COPY gradlew .
COPY gradle/wrapper/gradle-wrapper.properties gradle/wrapper/gradle-wrapper.properties

# gradle 다운로드 캐시
RUN ./gradlew --version

# 이제 소스 전체 복사
COPY . .

ARG TARGET
RUN ./gradlew :${TARGET}:build -x test

FROM amazoncorretto:21.0.4
WORKDIR /app
ARG TARGET
COPY --from=builder /app/${TARGET}/build/libs/*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
