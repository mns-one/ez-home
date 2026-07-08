# Build stage
FROM maven:3.9.9-eclipse-temurin AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:25-jre AS runner

WORKDIR /app

COPY --from=builder /app/target/*.jar ./app.jar

ENTRYPOINT ["java","-jar","app.jar"]
