# --- ETAPA 1: O Build (Compila o projeto) ---
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copia tudo da sua máquina para dentro do container
COPY . .

# Dá permissão ao Maven wrapper e compila o projeto ignorando testes para ser rápido
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# --- ETAPA 2: A Execução (Roda apenas o .jar gerado) ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia o .jar gerado na etapa anterior para a pasta atual
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]