FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Копируем только артефакт (предварительно соберите: mvn clean package -DskipTests)
COPY target/bank_rest-0.0.1-SNAPSHOT.jar app.jar

# Создаём non-root пользователя для безопасности
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]