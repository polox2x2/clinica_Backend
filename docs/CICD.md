# CI/CD — MediCitas Backend

Pipeline de integración y despliegue continuo para el backend (Spring Boot 4 / Java 21 / MySQL).
Definido en [`.github/workflows/ci-cd.yml`](../.github/workflows/ci-cd.yml).

## Modelo de ramas

```
feature/*  ──PR──▶  develop  ──PR──▶  staging  ──PR──▶  main
                       │                 │                │
                   solo valida       valida +         valida +
                   (no imagen)       publica imagen   publica imagen
                                     (sin desplegar)  + despliega PROD
```

- **Promoción por artefacto**: la imagen se construye una vez y se etiqueta con el SHA del commit; esa misma imagen es la que se despliega. No se recompila entre entornos.
- Triggers: `push` y `pull_request` sobre `main`, `staging`, `develop`.
- `concurrency` con `cancel-in-progress`: una corrida nueva cancela la anterior del mismo ref.

## Jobs

| Job | Depende de | Cuándo corre | Qué hace |
|---|---|---|---|
| `secretos` | — | siempre | Gitleaks: detecta credenciales commiteadas |
| `pruebas` | — | siempre | `package` → `test` → `verify -Pintegration` (Testcontainers+MySQL) → reporte JUnit + cobertura JaCoCo en PRs |
| `calidad` | `pruebas` | siempre (salvo PRs de Dependabot) | SonarQube self-hosted + Quality Gate |
| `dependencias` | `pruebas` | siempre | OWASP Dependency-Check (corta si CVSS ≥ 8) + reporte HTML |
| `imagen` | `secretos`, `calidad`, `dependencias` | push a `main`/`staging` | build multi-stage → Trivy (escanea antes de publicar) → push a GHCR (tags SHA + rama) |
| `deploy-produccion` | `imagen` | push a `main` | Deploy en Railway con la imagen GHCR → health check → rollback al SHA anterior si falla |

## Secrets requeridos

Configurar en **GitHub → Settings → Secrets and variables → Actions**.

| Secret | De dónde sale |
|---|---|
| `SONAR_HOST_URL` | URL pública de tu SonarQube (ej. `https://sonarqube.lmart.dev`), sin `/` final |
| `SONAR_TOKEN` | SonarQube → My Account → Security → Generate Token (proyecto `MedicitasBackend`) |
| `NVD_API_KEY` | Gratis en https://nvd.nist.gov/developers/request-an-api-key |
| `RAILWAY_TOKEN` | Railway → proyecto → Settings → Tokens → Create Token (env production) |
| `RAILWAY_SERVICE` | Nombre del servicio backend en Railway (ej. `medicitas-api`) |
| `PRODUCTION_URL` | Dominio público del backend (ej. `https://api.midominio.com`). Opcional al primer deploy: si falta, el health check se omite |

> `GITHUB_TOKEN` es automático (no hay que crearlo). Usa `packages: write` solo en el job `imagen`.

## GitHub Environment `production`

El deploy usa un environment llamado `production` como **gate de aprobación manual**:

1. Repo → **Settings → Environments → New environment** → `production`.
2. Activa **Required reviewers** (tú o el líder técnico).

Cada deploy a `main` quedará en pausa esperando aprobación registrada.

## Configuración en Railway

Un solo proyecto, un entorno (`production`), varios servicios:

```
Proyecto: medicitas / production
├── medicitas-api   (imagen GHCR)  ──▶  MySQL (red privada)
├── medicitas-front
└── MySQL           (servicio gestionado, con volumen)
```

### Servicio backend (medicitas-api)
- **Source**: Docker Image → `ghcr.io/<repo-en-minusculas>/medicitas-api`, leyendo el tag desde la variable `IMAGE_TAG` (el pipeline la setea con el SHA en cada deploy).
- Imagen privada de GHCR → Railway pide credenciales: usuario de GitHub + un **PAT con `read:packages`**.
- **Variables** del servicio (conexión a la BD por red privada):
  - `DB_URL` = `jdbc:mysql://${{MySQL.RAILWAY_PRIVATE_DOMAIN}}:3306/${{MySQL.MYSQL_DATABASE}}`
  - `DB_USERNAME` = `${{MySQL.MYSQL_USER}}`
  - `DB_PASSWORD` = `${{MySQL.MYSQL_PASSWORD}}`
  - `CORS_ALLOWED_ORIGINS` = dominio(s) del front, separados por coma
  - `JPA_DDL_AUTO` = `validate` (recomendado en prod)

### Dominio propio (Cloudflare)
1. Railway: servicio backend → Settings → Networking → **Custom Domain** → `api.midominio.com`.
2. Railway te da un **CNAME target** (`xxxx.up.railway.app`).
3. Cloudflare → DNS → **CNAME** `api` → ese target, en **DNS only** (nube gris) para que Railway emita el SSL.
4. Actualiza el secret `PRODUCTION_URL` a `https://api.midominio.com`.

## Notas de seguridad

- **OWASP** corta el build si hay CVE con CVSS ≥ 8. Overrides de versión aplicados en `pom.xml` (`tomcat.version`, `mysql.version`) para tapar CVEs de dependencias transitivas. Si aparecen CVEs sin parche upstream, alternativas: subir la versión, usar un `suppression.xml`, o pasar a report-only.
- **Trivy** escanea la imagen (CRITICAL/HIGH) **antes** de publicarla en GHCR. `ignore-unfixed: true`.
- **Dependabot** ([`.github/dependabot.yml`](../.github/dependabot.yml)) mantiene dependencias al día (maven semanal, actions/docker mensual, agrupados). Para arreglos automáticos de vulnerabilidades, activar en **Settings → Code security**: *Dependabot alerts* + *Dependabot security updates*.

## Desarrollo local

### Ejecutar el stack completo (API + MySQL)
```bash
cp .env.example .env      # ajusta valores
docker compose -f docker-compose.staging.yml up -d
curl http://localhost:8080/actuator/health   # {"status":"UP"}
```

### Correr los tests (necesitan Docker para Testcontainers)
```bash
./mvnw clean verify              # unitarias + cobertura
./mvnw verify -Pintegration      # + tests de integracion (*IT)
```

> **Windows + Docker Desktop reciente**: si Testcontainers no encuentra el daemon, exporta
> `DOCKER_HOST=npipe:////./pipe/docker_engine` antes de correr Maven.

## Variables de entorno configurables

Todas tienen default en `application.yaml`; ver [`.env.example`](../.env.example) para la lista completa:
`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DB_DRIVER`, `JPA_DDL_AUTO`, `JPA_SHOW_SQL`,
`JPA_FORMAT_SQL`, `JPA_DIALECT`, `PORT`, `ACTUATOR_ENDPOINTS`, `ACTUATOR_HEALTH_DETAILS`,
`CORS_ALLOWED_ORIGINS`, `SPRING_PROFILES_ACTIVE`.
