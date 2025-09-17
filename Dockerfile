# Etapa de build (compila el JAR)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# Etapa de runtime (imagen liviana con JRE)
FROM eclipse-temurin:17-jre
WORKDIR /app
# Si tu jar final se llama auth-api.jar, cópialo directo. Si no, usa comodín:
COPY --from=build /workspace/target/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
