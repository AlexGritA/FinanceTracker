package com.financetracker.security;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Secret key for signing tokens - generated once on startup
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // Token expiration time - 24 hours in milliseconds
    private final long EXPIRATION = 1000 * 60 * 60 * 24;

    // Generates a JWT token with username as subject
    public String generateToken(String username) {
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                    .signWith(key)
                    .compact();
    }
    // Extracts username from a JWT token
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Validates token by comparing extracted username with expected username
    public Boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username);
    }
}
