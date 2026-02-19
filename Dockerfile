# 1. Build stage: Gradle을 사용해 빌드 (이미 빌드된 JAR가 있다면 생략 가능)
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean bootJar -x test

# 2. Run stage: 실행용 이미지 생성
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# build 단계에서 생성된 jar 파일을 복사 (프로젝트명-버전.jar 형태)
COPY --from=build /app/build/libs/*.jar app.jar

# 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
