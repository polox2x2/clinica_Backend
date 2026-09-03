# ============================================================================
# Stage 1: build — compila, empaqueta el jar y crea un JRE minimo con jlink
# ============================================================================
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiamos primero solo el pom para cachear las dependencias en su propia capa:
# mientras el pom no cambie, Docker reutiliza esta capa y no re-descarga todo.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Ahora el codigo fuente y el empaquetado (los tests corren en el pipeline, no aqui)
COPY src ./src
RUN mvn clean package -DskipTests -B

# JRE recortado a medida: reduce ~200MB de JRE completo a ~70MB.
# Se incluye un conjunto de modulos suficiente para Spring Boot + JPA/MySQL + TLS.
RUN jlink \
      --add-modules java.base,java.logging,java.naming,java.management,java.instrument,java.sql,java.net.http,java.xml,java.desktop,java.security.jgss,java.compiler,java.prefs,jdk.crypto.ec,jdk.crypto.cryptoki,jdk.unsupported,jdk.jdwp.agent \
      --strip-debug --no-man-pages --no-header-files --compress=2 \
      --output /javaruntime

# ============================================================================
# Stage 2: runtime — alpine minimo + JRE recortado + jar
# ============================================================================
FROM alpine:3.24 AS runtime
WORKDIR /app

# wget para el HEALTHCHECK; el JRE de jlink (temurin/musl) corre sobre alpine sin mas libs
RUN apk add --no-cache wget

# JRE recortado copiado desde el stage de build
ENV JAVA_HOME=/opt/java
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=build /javaruntime ${JAVA_HOME}

# Usuario no-root para ejecutar la aplicacion
RUN addgroup -S spring && adduser -S spring -G spring -u 1001
USER spring:spring

# Copiamos SOLO el jar empaquetado desde el stage de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Verifica el estado de la app via Actuator (endpoint publico)
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1

# UseContainerSupport + MaxRAMPercentage: la JVM respeta los limites del contenedor
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
