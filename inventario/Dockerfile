# Etapa 1: Compilación (Build)
# Usamos una imagen de Maven compatible con Java 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copiar el archivo de proyecto para descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copiar el código fuente y compilar el archivo JAR
COPY src ./src
RUN mvn clean package -DskipTests -Dfile.encoding=UTF-8

# Etapa 2: Imagen de Ejecución (Runtime)
# Usamos el JRE de Java 21 para una imagen más ligera y segura
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiar el JAR generado desde la etapa anterior
# Se usa un comodín (*) para encontrar el JAR independientemente del nombre del proyecto
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto estándar de Spring Boot
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]