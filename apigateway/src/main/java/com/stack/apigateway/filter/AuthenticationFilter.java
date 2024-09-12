package com.stack.apigateway.filter;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {


    public static class Config {
        // Configuration properties (if needed)
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(AuthenticationFilter.Config config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (token == null || !token.startsWith("Bearer ")) {
                return unauthorizedResponse(exchange);
            }

            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            String validationUrl = "http://localhost:8082/auth/validateToken"; // Auth service URL

            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        validationUrl,
                        HttpMethod.GET,
                        new org.springframework.http.HttpEntity<>(new HttpHeaders() {{
                            set(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
                        }}),
                        String.class
                );

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JsonNode jsonNode = objectMapper.readTree(response.getBody());
                    String username = jsonNode.get("username").asText();

                    ServerWebExchange modifiedExchange = exchange.mutate()
                            .request(exchange.getRequest().mutate()
                                    .header("X-User-Id", username) // Add username to headers
                                    .build())
                            .build();

                    return chain.filter(modifiedExchange);
                } else {
                    return unauthorizedResponse(exchange);
                }
            } catch (Exception e) {
                return unauthorizedResponse(exchange);
            }
        };
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}