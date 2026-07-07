package com.ezhome.apigateway.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtUtil {

  private final SecretKey secretKey;

  public JwtUtil(@Value("${jwt.secret}") String secret) {
    byte[] keyBytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
  }

  public Claims extractClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
