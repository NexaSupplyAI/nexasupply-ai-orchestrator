package com.nexasupplyai.ai.agent;

import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.nexasupplyai.ai.prompt.PromptsSistema;
import com.nexasupplyai.ai.exception.FoundryUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.*;
import java.net.http.HttpClient;
import java.time.Duration;

@Component
public class FoundryResponsesClient {
    private final boolean enabled;
    private final String modelo;
    private final String tokenScope;
    private final int maxRondas;
    private final int maxTokensSalida;
    private final int maxMensajesHistorial;
    private final int maxCaracteresHistorial;
    private final RestClient cliente;
    private final DefaultAzureCredential credencial;
    private final ObjectMapper objectMapper;
    private final AgentToolRegistry herramientas;

    public FoundryResponsesClient(ObjectMapper objectMapper, AgentToolRegistry herramientas,
                                  @Value("${foundry.enabled:false}") boolean enabled,
                                  @Value("${foundry.project-endpoint:}") String endpoint,
                                  @Value("${foundry.model:gpt-5-mini}") String modelo,
                                  @Value("${foundry.token-scope:https://cognitiveservices.azure.com/.default}") String tokenScope,
                                  @Value("${foundry.max-tool-rounds:4}") int maxRondas,
                                  @Value("${foundry.max-output-tokens:800}") int maxTokensSalida,
                                  @Value("${foundry.max-history-messages:12}") int maxMensajesHistorial,
                                  @Value("${foundry.max-history-characters:24000}") int maxCaracteresHistorial,
                                  @Value("${foundry.connect-timeout:5s}") Duration connectTimeout,
                                  @Value("${foundry.read-timeout:60s}") Duration readTimeout) {
        this.enabled = enabled;
        this.modelo = modelo;
        this.tokenScope = tokenScope;
        this.maxRondas = maxRondas;
        this.maxTokensSalida = maxTokensSalida;
        this.maxMensajesHistorial = maxMensajesHistorial;
        this.maxCaracteresHistorial = maxCaracteresHistorial;
        this.objectMapper = objectMapper;
        this.herramientas = herramientas;
        if (enabled && (endpoint == null || endpoint.isBlank()))
            throw new IllegalStateException("FOUNDRY_PROJECT_ENDPOINT es obligatorio cuando FOUNDRY_ENABLED=true");
        if (enabled && (modelo == null || modelo.isBlank()))
            throw new IllegalStateException("FOUNDRY_MODEL es obligatorio cuando FOUNDRY_ENABLED=true");
        if (maxRondas < 1) throw new IllegalArgumentException("FOUNDRY_MAX_TOOL_ROUNDS debe ser mayor que cero");
        if (maxTokensSalida < 1) throw new IllegalArgumentException("FOUNDRY_MAX_OUTPUT_TOKENS debe ser mayor que cero");
        if (maxMensajesHistorial < 1) throw new IllegalArgumentException("FOUNDRY_MAX_HISTORY_MESSAGES debe ser mayor que cero");
        if (maxCaracteresHistorial < 1) throw new IllegalArgumentException("FOUNDRY_MAX_HISTORY_CHARACTERS debe ser mayor que cero");
        if (enabled) {
            var factory = new JdkClientHttpRequestFactory(HttpClient.newBuilder()
                    .connectTimeout(connectTimeout).build());
            factory.setReadTimeout(readTimeout);
            this.cliente = RestClient.builder().baseUrl(endpoint).requestFactory(factory).build();
        } else {
            this.cliente = null;
        }
        this.credencial = enabled ? new DefaultAzureCredentialBuilder().build() : null;
    }

    public Resultado responder(List<Map<String, Object>> historial) {
        if (!enabled) throw new FoundryUnavailableException();
        List<Map<String, Object>> invocaciones = new ArrayList<>();
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("model", modelo);
        cuerpo.put("instructions", PromptsSistema.ASISTENTE_NEXA_SUPPLY + "\n"
                + PromptsSistema.INVENTARIO + PromptsSistema.PEDIDOS + PromptsSistema.PROVEEDORES + PromptsSistema.REPORTES);
        cuerpo.put("input", limitarHistorial(historial));
        cuerpo.put("tools", herramientas.definiciones());
        cuerpo.put("max_output_tokens", maxTokensSalida);

        Map<String, Object> respuesta = invocar(cuerpo);
        for (int ronda = 0; ronda < maxRondas; ronda++) {
            List<Map<String, Object>> llamadas = llamadasHerramienta(respuesta);
            if (llamadas.isEmpty()) return resultadoFinal(respuesta, invocaciones);
            List<Map<String, Object>> salidas = new ArrayList<>();
            for (Map<String, Object> llamada : llamadas) {
                String nombre = String.valueOf(llamada.get("name"));
                String argumentosJson = String.valueOf(llamada.getOrDefault("arguments", "{}"));
                Map<String, Object> argumentos;
                try {
                    argumentos = objectMapper.readValue(argumentosJson, new TypeReference<>() {});
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Argumentos inválidos para " + nombre, ex);
                }
                long inicio = System.nanoTime();
                try {
                    Map<String, Object> salida = herramientas.ejecutar(nombre, argumentos);
                    int duracion = (int) ((System.nanoTime() - inicio) / 1_000_000L);
                    invocaciones.add(Map.of("nombre", nombre, "argumentos", argumentos,
                            "respuesta", salida, "duracionMs", duracion, "estado", "SUCCESS"));
                    salidas.add(Map.of("type", "function_call_output", "call_id", llamada.get("call_id"),
                            "output", objectMapper.writeValueAsString(salida)));
                } catch (Exception ex) {
                    int duracion = (int) ((System.nanoTime() - inicio) / 1_000_000L);
                    invocaciones.add(Map.of("nombre", nombre, "argumentos", argumentos,
                            "respuesta", Map.of(), "duracionMs", duracion, "estado", "FAILED",
                            "error", ex.getMessage() == null ? "Error" : ex.getMessage()));
                    salidas.add(Map.of("type", "function_call_output", "call_id", llamada.get("call_id"),
                            "output", "{\"error\":\"No se pudo ejecutar la herramienta\"}"));
                }
            }
            cuerpo = new LinkedHashMap<>();
            cuerpo.put("model", modelo);
            cuerpo.put("previous_response_id", respuesta.get("id"));
            cuerpo.put("input", salidas);
            cuerpo.put("tools", herramientas.definiciones());
            cuerpo.put("max_output_tokens", maxTokensSalida);
            respuesta = invocar(cuerpo);
        }
        throw new IllegalStateException("El agente superó el máximo de rondas de herramientas");
    }

    private Map<String, Object> invocar(Map<String, Object> cuerpo) {
        String token = Objects.requireNonNull(credencial.getToken(
                new TokenRequestContext().addScopes(tokenScope)).block()).getToken();
        Map<String, Object> respuesta = cliente.post().uri("/openai/v1/responses")
                .headers(headers -> headers.setBearerAuth(token)).body(cuerpo).retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<>() {});
        if (respuesta == null) throw new IllegalStateException("Foundry devolvió una respuesta vacía");
        return respuesta;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> llamadasHerramienta(Map<String, Object> respuesta) {
        Object output = respuesta.get("output");
        if (!(output instanceof List<?> lista)) return List.of();
        return lista.stream().filter(Map.class::isInstance).map(v -> (Map<String, Object>) v)
                .filter(v -> "function_call".equals(v.get("type"))).toList();
    }

    @SuppressWarnings("unchecked")
    private Resultado resultadoFinal(Map<String, Object> respuesta, List<Map<String, Object>> invocaciones) {
        String texto = respuesta.get("output_text") instanceof String s ? s : null;
        if (texto == null) {
            List<String> partes = new ArrayList<>();
            Object output = respuesta.get("output");
            if (output instanceof List<?> lista) for (Object item : lista) {
                if (item instanceof Map<?, ?> mapa && mapa.get("content") instanceof List<?> contenidos)
                    for (Object contenido : contenidos)
                        if (contenido instanceof Map<?, ?> c && c.get("text") != null) partes.add(String.valueOf(c.get("text")));
            }
            texto = String.join("\n", partes);
        }
        if (texto == null || texto.isBlank()) texto = "Foundry no produjo contenido textual.";
        Map<String, Object> uso = respuesta.get("usage") instanceof Map<?, ?> m ? (Map<String, Object>) m : Map.of();
        return new Resultado(texto, modelo, entero(uso.get("input_tokens")), entero(uso.get("output_tokens")), invocaciones);
    }

    private int entero(Object valor) { return valor instanceof Number n ? n.intValue() : 0; }

    List<Map<String, Object>> limitarHistorial(List<Map<String, Object>> historial) {
        if (historial == null || historial.isEmpty()) return List.of();
        LinkedList<Map<String, Object>> resultado = new LinkedList<>();
        int caracteres = 0;
        for (int i = historial.size() - 1; i >= 0 && resultado.size() < maxMensajesHistorial; i--) {
            Map<String, Object> mensaje = historial.get(i);
            int longitud = String.valueOf(mensaje.getOrDefault("content", "")).length();
            if (!resultado.isEmpty() && caracteres + longitud > maxCaracteresHistorial) break;
            resultado.addFirst(mensaje);
            caracteres += longitud;
        }
        return List.copyOf(resultado);
    }

    public record Resultado(String texto, String modelo, int tokensEntrada, int tokensSalida,
                            List<Map<String, Object>> invocaciones) {}
}
