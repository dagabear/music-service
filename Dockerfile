# Этап 1: Сборка приложения
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /application
COPY . .
# Сборка JAR файла (пропускаем тесты для скорости, но можно убрать -x test)
RUN ./gradlew clean bootJar -x test

# Этап 2: Извлечение слоев
FROM eclipse-temurin:25-jdk-alpine AS extractor
WORKDIR /application
COPY --from=builder /application/build/libs/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Этап 3: Финальный образ для рантайма
FROM eclipse-temurin:25-jre-alpine
WORKDIR /application

# Копируем извлеченные слои
COPY --from=extractor /application/dependencies/ ./
COPY --from=extractor /application/spring-boot-loader/ ./
COPY --from=extractor /application/snapshot-dependencies/ ./
COPY --from=extractor /application/application/ ./

# Настройка порта (Spring Boot по умолчанию 8080)
EXPOSE 8080

# Оптимальный запуск через JarLauncher
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]