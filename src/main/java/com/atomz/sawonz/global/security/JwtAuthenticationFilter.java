package com.atomz.sawonz.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String token = resolveToken(req); // Authorization → Cookie(AT) 순

        if (token != null && jwtTokenProvider.validate(token)) {
            UsernamePasswordAuthenticationToken auth = jwtTokenProvider.parseAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(req, res);
    }

    private String resolveToken(HttpServletRequest req) {
        String authz = req.getHeader("Authorization");
        if (authz != null && authz.startsWith("Bearer ")) {
            return authz.substring(7);
        }
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if ("AT".equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }
}