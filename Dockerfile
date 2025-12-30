# Imagem base com Maven e Java 17
FROM maven:3.9-eclipse-temurin-17

# Diretório de trabalho
WORKDIR /app

# Copia todo o projeto
COPY . .

# Compila o projeto
RUN mvn clean package -DskipTests

# Porta da aplicação
EXPOSE 8080

# Executa a aplicação
CMD ["java", "-jar", "target/todolist-microservice-1.0.0.jar"]
