package com.atomz.sawonz.global.security;

import com.atomz.sawonz.global.exception.HttpCustomResponse;
import com.atomz.sawonz.global.exception.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper(); // 필요시 스프링 ObjectMapper 주입으로 교체 가능

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        ResponseCode code = (ResponseCode) request.getAttribute("auth_error_code");
        if (code == null) code = ResponseCode.TOKEN_INVALID;

        HttpCustomResponse<Void> body = new HttpCustomResponse<>(code, code.getDescription());

        response.setStatus(code.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(response.getWriter(), body);
    }
}