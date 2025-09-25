package com.atomz.sawonz.global.util;

import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CookieUtil {

    @Value("${app.cookie.domain}")
    private String domain;
    @Value("${app.cookie.secure}")
    private boolean secure;
    @Value("${app.cookie.same-site}")
    private String sameSite;
    @Value("${app.cookie.path:/}")
    private String path;

    public ResponseCookie buildHttpOnlyCookie(String name, String value, long maxAgeSeconds) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite) // "None", "Lax", "Strict"
                .domain(domain)
                .path(path)
                .maxAge(maxAgeSeconds)
                .build();
    }

    public ResponseCookie clearCookie(String name) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .domain(domain)
                .path(path)
                .maxAge(0)
                .build();
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}