# AI Orchestrator

Orquestador de IA de NexaSupply AI. Es propietario del esquema PostgreSQL `ai` para conversaciones, mensajes, ejecuciones de herramientas y auditoría.

## Responsabilidades

- Crear y consultar conversaciones.
- Invocar Microsoft Foundry mediante Responses API y Managed Identity.
- Ejecutar exclusivamente herramientas de dominio registradas.
- Persistir automáticamente mensajes, ejecuciones y auditoría.
- Propagar `Authorization` y `X-Correlation-ID` a los servicios de dominio.

## Configuración

| Variable | Obligatoria | Valor local predeterminado |
|---|---:|---|
| `DB_URL` | No | `jdbc:postgresql://localhost:5432/nexasupplyai` |
| `DB_USER` | No | `postgres` |
| `DB_PASSWORD` | No | `postgres` |
| `JWT_ISSUER_URI` | Producción | `http://localhost:9999` |
| `JWT_JWK_SET_URI` | Producción | `http://localhost:9999/.well-known/jwks.json` |
| `APP_SECURITY_ENABLED` | No | `true` |
| `CORS_ALLOWED_ORIGINS` | No | `http://localhost:4200` |
| `INVENTORY_SERVICE_URL` | No | `http://localhost:8081` |
| `ORDER_SERVICE_URL` | No | `http://localhost:8082` |
| `SUPPLIER_SERVICE_URL` | No | `http://localhost:8083` |
| `REPORTING_SERVICE_URL` | No | `http://localhost:8084` |
| `FOUNDRY_ENABLED` | No | `false` |
| `FOUNDRY_PROJECT_ENDPOINT` | Si Foundry está activo | Sin valor |
| `FOUNDRY_MODEL` | No | `gpt-5-mini` |

Inicializa la base con [`../nexasupplyai.sql`](../nexasupplyai.sql).

## Ejecución

```bash
export JWT_ISSUER_URI="https://login.microsoftonline.com/<tenant-id>/v2.0"
./mvnw spring-boot:run
```

Puerto: `8085`. Salud pública: `GET /actuator/health`.

### Docker

```bash
docker build -t nexasupply/ai-orchestrator:local .
docker run --rm --add-host=host.docker.internal:host-gateway \
  -p 8085:8085 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/nexasupplyai \
  -e DB_USER=postgres \
  -e DB_PASSWORD=postgres \
  -e JWT_ISSUER_URI=https://login.microsoftonline.com/<tenant-id>/v2.0 \
  -e INVENTORY_SERVICE_URL=http://host.docker.internal:8081 \
  -e ORDER_SERVICE_URL=http://host.docker.internal:8082 \
  -e SUPPLIER_SERVICE_URL=http://host.docker.internal:8083 \
  -e REPORTING_SERVICE_URL=http://host.docker.internal:8084 \
  nexasupply/ai-orchestrator:local
```

## API

| Método | Ruta | Uso |
|---|---|---|
| `GET` | `/api/v1/conversaciones?idUsuario={entraObjectId}` | Listar conversaciones |
| `POST` | `/api/v1/conversaciones` | Crear conversación |
| `GET` | `/api/v1/conversaciones/{id}` | Consultar conversación |
| `GET` | `/api/v1/mensajes?idConversacion={uuid}` | Listar mensajes |
| `GET` | `/api/v1/mensajes/{id}` | Consultar mensaje |
| `GET` | `/api/v1/ejecuciones-herramienta?idConversacion={uuid}` | Listar ejecuciones |
| `GET` | `/api/v1/ejecuciones-herramienta/{id}` | Consultar ejecución |
| `GET` | `/api/v1/eventos-auditoria?idUsuario={entraObjectId}` | Listar auditoría |
| `GET` | `/api/v1/eventos-auditoria/{id}` | Consultar evento |
| `POST` | `/api/v1/chat` | Enviar mensaje; crea o continúa una conversación |

Estados de conversación: `ACTIVE`, `CLOSED`, `ARCHIVED`. Roles: `USER`, `ASSISTANT`, `SYSTEM`, `TOOL`.

## Pruebas

```bash
./mvnw clean test
./mvnw clean package
```

## Integración Foundry

El cliente obtiene un token Entra para `https://ai.azure.com/.default` con `DefaultAzureCredential` y llama a `{FOUNDRY_PROJECT_ENDPOINT}/openai/v1/responses`. Procesa ciclos de `function_call` con un máximo configurable y solo permite estas herramientas de consulta: stock de producto, stock bajo, pedidos pendientes, proveedor y dashboard. Las conversaciones se aíslan por el `sub` del JWT.

Cuando `FOUNDRY_ENABLED=false`, existe un fallback local determinista para demostrar consultas de stock, pedidos y dashboard sin consumir un modelo. No se exponen herramientas destructivas ni compras automáticas.
