package com.ezhome.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ezhome.apigateway.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

  private final JwtUtil jwtUtil;

  public JwtValidationGatewayFilterFactory(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  public GatewayFilter apply(Object config) {

    return (exchange, chain) -> {
      // verify token and attach claims to request header for downstream services
      
      String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

      if(authHeader == null || !authHeader.startsWith("Bearer ")) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      }

      String token = authHeader.substring(7);

      try {
        Claims claims = jwtUtil.extractClaims(token);
        String id = claims.getSubject();
        String role = claims.get("role", String.class);

        var mutatedRequest = exchange.getRequest().mutate()
            .header("X-User-Id", id)
            .header("X-User-Role", role)
            .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
      }
      catch (JwtException e) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      }

    };

  }

}

