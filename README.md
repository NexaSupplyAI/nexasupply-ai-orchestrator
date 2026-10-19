# AI Orchestrator

API propietaria del esquema `ai`, puerto `8085`. Persiste conversaciones, mensajes, ejecuciones de tools y eventos de auditoría.

`POST /api/v1/ai/chat` sigue el flujo Responses API → `function_call` → registry → cliente REST → `function_call_output` → respuesta. Tools: `get_product_stock`, `get_low_stock_products`, `get_pending_orders`, `get_supplier_information` y `get_dashboard`. No acceden a bases ajenas ni ejecutan acciones destructivas.

Foundry usa `DefaultAzureCredential` con `FOUNDRY_PROJECT_ENDPOINT`, `FOUNDRY_MODEL`, `FOUNDRY_TOKEN_SCOPE` y los límites `FOUNDRY_MAX_TOOL_ROUNDS`, `FOUNDRY_MAX_OUTPUT_TOKENS`, `FOUNDRY_MAX_HISTORY_MESSAGES` y `FOUNDRY_MAX_HISTORY_CHARACTERS`. Con `FOUNDRY_ENABLED=false`, la aplicación arranca y chat devuelve `503 FOUNDRY_NOT_CONFIGURED`; health, persistencia y APIs de consulta siguen disponibles.

Las APIs de conversaciones, mensajes, tools y auditoría verifican propietario; `ADMIN` puede consultar transversalmente. Variables adicionales: las cuatro URLs internas, timeouts y autenticación interservicio.

```bash
export SPRING_PROFILES_ACTIVE=local DB_URL=jdbc:postgresql://localhost:5432/nexastock DB_USER=postgres DB_PASSWORD='<local>' FOUNDRY_ENABLED=false
./mvnw clean test
```

OpenAPI: `/v3/api-docs`; health: `/actuator/health`.
