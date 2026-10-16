# AI Orchestrator

Base del orquestador de IA de NexaSupply AI. Es propietario del esquema PostgreSQL `ai` para conversaciones, mensajes, ejecuciones de herramientas y auditoría.

## Responsabilidades actuales

- Crear y consultar conversaciones.
- Consultar mensajes, ejecuciones de herramientas y eventos de auditoría ya almacenados.
- Exponer adaptadores Java para consultar inventario, pedidos, proveedores y reportes.
- Propagar `Authorization` y `X-Correlation-ID` a los servicios de dominio.

## Configuración

| Variable | Obligatoria | Valor local predeterminado |
|---|---:|---|
| `DB_URL` | No | `jdbc:postgresql://localhost:5432/nexasupplyai` |
| `DB_USER` | No | `postgres` |
| `DB_PASSWORD` | No | `postgres` |
| `JWT_ISSUER_URI` | Sí | Sin valor |
| `CORS_ALLOWED_ORIGINS` | No | `http://localhost:4200` |
| `INVENTORY_SERVICE_URL` | No | `http://localhost:8081` |
| `ORDER_SERVICE_URL` | No | `http://localhost:8082` |
| `SUPPLIER_SERVICE_URL` | No | `http://localhost:8083` |
| `REPORTING_SERVICE_URL` | No | `http://localhost:8084` |
| `FLYWAY_ENABLED` | No | `false` |

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

## API actual

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

Estados de conversación: `ACTIVE`, `CLOSED`, `ARCHIVED`. Roles: `USER`, `ASSISTANT`, `SYSTEM`, `TOOL`.

## Pruebas

```bash
./mvnw clean test
./mvnw clean package
```

## Estado de la integración de IA

Este módulo todavía **no integra Microsoft Foundry Agent Service ni un modelo**. Las clases `agent` y `tool` son adaptadores locales y no existe todavía un endpoint de chat que invoque un agente. Antes de producción faltan:

- Cliente/SDK de Foundry y configuración por Managed Identity.
- Registro real de tools con contratos tipados; evitar `Map` sin tipo.
- Endpoint de chat o streaming.
- Persistencia automática de mensajes, tool calls y auditoría.
- Autorización por roles y comprobación de que el usuario solo consulta sus conversaciones.
- Confirmación humana para acciones como crear borradores de compra.
