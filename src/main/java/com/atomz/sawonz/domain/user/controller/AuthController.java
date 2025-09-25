package com.atomz.sawonz.domain.user.controller;

import com.atomz.sawonz.domain.user.dto.AuthDto.LoginRequest;
import com.atomz.sawonz.domain.user.dto.AuthDto.MeResponse;
import com.atomz.sawonz.domain.user.service.AuthService;
import com.atomz.sawonz.global.exception.ErrorException;
import com.atomz.sawonz.global.exception.HttpCustomResponse;
import com.atomz.sawonz.global.exception.ResponseCode;
import com.atomz.sawonz.global.security.CustomUserPrincipal;
import com.atomz.sawonz.global.util.CookieUtil;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @Value("${app.jwt.access-exp-min}")
    private long accessExpMin;

    @Value("${app.jwt.refresh-exp-min}")
    private long refreshExpMin;

    @PostMapping("/login")
    public ResponseEntity<HttpCustomResponse<Void>> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {

        var tokens = authService.login(loginRequest);

        // (A안) 설정값 기준으로 쿠키 수명 지정
        long atMaxAgeSec = accessExpMin * 60;
        long rtMaxAgeSec = refreshExpMin * 60;

        ResponseCookie atCookie = cookieUtil.buildHttpOnlyCookie("AT", tokens.getAccessToken(), atMaxAgeSec);
        ResponseCookie rtCookie = cookieUtil.buildHttpOnlyCookie("RT", tokens.getRefreshToken(), rtMaxAgeSec);

        HttpCustomResponse<Void> body =
                new HttpCustomResponse<>(ResponseCode.SUCCESS, "로그인 성공");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, atCookie.toString())
                .header(HttpHeaders.SET_COOKIE, rtCookie.toString())
                .body(body);
    }

    @GetMapping("/me")
    public ResponseEntity<HttpCustomResponse<MeResponse>> me(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {

        if (principal == null) {
            throw new ErrorException(ResponseCode.TOKEN_INVALID);
        }

        // 요청별 principal 로깅
        log.info("[/auth/me] principal - id={}, email={}, userName={}, role={}",
                principal.getId(),
                principal.getEmail(),
                principal.getUsername(),
                principal.getRole()
        );

        return ResponseEntity.ok(
                new HttpCustomResponse<>(ResponseCode.SUCCESS, authService.me(principal))
        );
    }

}
