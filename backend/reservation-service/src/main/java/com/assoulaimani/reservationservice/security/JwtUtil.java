package com.assoulaimani.reservationservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    // ✅ EXACTEMENT la même clé que dans user-service
    private static final String SECRET_KEY = "your-secret-key-must-be-at-least-256-bits-long-for-hs256";


    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // ✅ Extraire le rôle
    public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }

    // ✅ Extraire l'email
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // ✅ Valider le token
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Token invalide: " + e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}