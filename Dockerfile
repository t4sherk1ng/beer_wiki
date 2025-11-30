# ====== STAGE 1: build ======
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Сначала копируем только pom.xml и качаем зависимости (кэшируется)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Теперь копируем остальной код и собираем jar
COPY src ./src
RUN mvn -B clean package -DskipTests

# ====== STAGE 2: runtime ======
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Копируем собранный jar из build-стейджа
COPY --from=build /app/target/*.jar app.jar

# Порт приложения (у тебя server.port=8081)
EXPOSE 8081

# Дополнительно: можно настроить JVM-опции через переменную
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]