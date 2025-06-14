ARG TARGET=board

FROM maven:3.9.9-amazoncorretto-21-debian-bookworm AS builder
WORKDIR /app

COPY . .

ARG TARGET
RUN ./gradlew :${TARGET}:build -x test
RUN ls -l /app/${TARGET}/build/libs/

FROM amazoncorretto:21.0.4
WORKDIR /app
ARG TARGET
COPY --from=builder /app/${TARGET}/build/libs/*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
