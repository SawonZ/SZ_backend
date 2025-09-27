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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/login")
    public ResponseEntity<HttpCustomResponse<Void>> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {

        var tokens = authService.login(loginRequest);

        ResponseCookie atCookie = cookieUtil.buildAt("AT", tokens.getAccessToken());
        ResponseCookie rtCookie = cookieUtil.buildRt("RT", tokens.getRefreshToken());

        HttpCustomResponse<Void> body =
                new HttpCustomResponse<>(ResponseCode.SUCCESS, "로그인 성공");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, atCookie.toString())
                .header(HttpHeaders.SET_COOKIE, rtCookie.toString())
                .body(body);
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpCustomResponse<Void>> logout() {
        ResponseCookie clearAT = cookieUtil.clearCookie("AT");
        ResponseCookie clearRT = cookieUtil.clearCookie("RT");

        HttpCustomResponse<Void> body = new HttpCustomResponse<>(ResponseCode.SUCCESS, "로그아웃 완료");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearAT.toString())
                .header(HttpHeaders.SET_COOKIE, clearRT.toString())
                .body(body);
    }

    @GetMapping("/me")
    public ResponseEntity<HttpCustomResponse<MeResponse>> me(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        if (principal == null) {
            throw new ErrorException(ResponseCode.TOKEN_INVALID);
        }
        return ResponseEntity.ok(
                new HttpCustomResponse<>(ResponseCode.SUCCESS, authService.me(principal))
        );
    }
}
