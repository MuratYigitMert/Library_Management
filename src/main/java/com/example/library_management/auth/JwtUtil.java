package com.example.library_management.auth;

import com.example.library_management.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private  Key signingKey;
    private  JwtParser jwtParser;
    @PostConstruct
    public void init() {
        signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        jwtParser = Jwts.parserBuilder().setSigningKey(signingKey).build();
    }
    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("username", user.getUsername());
        claims.put("authorities", List.of(user.getRole().getName())); // e.g., ROLE_USER

        Date now = new Date();
        Date expiry = new Date(now.getTime() + TimeUnit.HOURS.toMillis(1)); // valid for 1 hour

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                log.debug("Token found: {}", token);
                return jwtParser.parseClaimsJws(token).getBody();
            } else {
                log.warn("Authorization header is missing or invalid.");
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            log.warn("Expired token: {}", ex.getMessage());
            throw ex;
        } catch (JwtException | IllegalArgumentException ex) {
            req.setAttribute("invalid", ex.getMessage());
            log.warn("Invalid token: {}", ex.getMessage());
            throw ex;
        }
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        boolean valid = claims.getExpiration().after(new Date());
        if (valid) {
            log.debug("Token is valid");
        } else {
            log.warn("Token is expired");
        }
        return valid;
    }

    public String getUsername(Claims claims) {
        return claims.getSubject(); // we use username as subject
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
