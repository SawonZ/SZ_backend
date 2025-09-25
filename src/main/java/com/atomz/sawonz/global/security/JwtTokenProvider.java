package com.atomz.sawonz.global.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.access-exp-min}")
    private long accessExpMin;

    @Value("${app.jwt.refresh-exp-min}")
    private long refreshExpMin;

    private Key key;

    @PostConstruct
    void init() {
        // secret은 Base64 문자열이라고 가정
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateAccessToken(String subjectEmail, String role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessExpMin * 60);
        return Jwts.builder()
                .setSubject(normalizeEmail(subjectEmail))
                .claim("role", role)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String subjectEmail, String role) {
        // 역할 변화에 즉시 반영하려면 RT에는 roles를 넣지 말고 DB조회/토큰버전검증을 하세요.
        // 여기서는 DB 미조회 요구에 맞춰 roles를 함께 넣습니다.
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(refreshExpMin * 60);
        return Jwts.builder()
                .setSubject(normalizeEmail(subjectEmail))
                .claim("role", role)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public UsernamePasswordAuthenticationToken parseAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String email = claims.getSubject();
        String role = claims.get("role", String.class);

        if (email == null || email.isBlank()) {
            throw new com.atomz.sawonz.global.exception.ErrorException(
                    com.atomz.sawonz.global.exception.ResponseCode.TOKEN_SUBJECT_MISSING
            );
        }
        if (role == null || role.isBlank()) {
            throw new com.atomz.sawonz.global.exception.ErrorException(
                    com.atomz.sawonz.global.exception.ResponseCode.TOKEN_ROLE_MISSING
            );
        }

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.trim()));

        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
