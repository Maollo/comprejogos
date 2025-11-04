# Estágio 1: Build (Construção)
# Usamos uma imagem Maven com Java 17 para compilar nosso projeto
# ESTA É A LINHA CORRIGIDA:
FROM maven:3.9-eclipse-temurin-17 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o pom.xml primeiro para aproveitar o cache do Docker
COPY pom.xml .

# Copia todo o código-fonte
COPY src ./src

# Roda o Maven para "empacotar" a aplicação, pulando os testes
# Isso vai gerar o arquivo .jar em /app/target/
RUN mvn clean package -DskipTests

# ---

# Estágio 2: Run (Execução)
# Usamos uma imagem JRE (Java Runtime) leve, que é menor e mais segura
# Esta imagem (a segunda) já estava correta
FROM eclipse-temurin:17-jre-jammy

# Define o diretório de trabalho
WORKDIR /app

# Copia APENAS o .jar compilado do Estágio 1 (build) para este novo container
# O nome do .jar deve bater com o artifactId e version do seu pom.xml
COPY --from=build /app/target/comprejogos-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080 (que o Spring Boot usa por padrão)
EXPOSE 8080

# Comando para executar a aplicação quando o container iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]