FROM gradle:9.2.1-jdk21 as builder
WORKDIR /builds
COPY ./ /builds
RUN gradle build -x test

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /builds/build/libs/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]