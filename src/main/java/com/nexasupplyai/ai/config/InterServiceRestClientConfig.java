package com.nexasupplyai.ai.config;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.UUID;

@Configuration
public class InterServiceRestClientConfig {

    @Bean
    RestClientCustomizer configurarClientes(InterServiceAuthenticationProvider auth,
            @Value("${clients.http.connect-timeout:3s}") Duration connect,
            @Value("${clients.http.read-timeout:10s}") Duration read) {
        var factory=new JdkClientHttpRequestFactory(HttpClient.newBuilder().connectTimeout(connect).build());
        factory.setReadTimeout(read);
        return builder -> builder.requestInterceptor((request, body, execution) -> {
            String id=MDC.get(CorrelationIdFilter.MDC_KEY);
            request.getHeaders().set(CorrelationIdFilter.HEADER,id==null?UUID.randomUUID().toString():id);
            auth.apply(request.getHeaders());
            return execution.execute(request, body);
        }).requestFactory(factory);
    }
}
