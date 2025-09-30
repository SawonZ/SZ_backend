package com.atomz.sawonz.domain.user.service;

import com.atomz.sawonz.domain.user.dto.AuthDto.LoginRequest;
import com.atomz.sawonz.domain.user.dto.AuthDto.MeResponse;
import com.atomz.sawonz.domain.user.dto.AuthDto.TokenPair;
import com.atomz.sawonz.domain.user.entity.UsersEntity;
import com.atomz.sawonz.domain.user.repository.UsersRepository;
import com.atomz.sawonz.global.exception.ErrorException;
import com.atomz.sawonz.global.exception.ResponseCode;
import com.atomz.sawonz.global.security.CustomUserPrincipal;
import com.atomz.sawonz.global.security.JwtTokenProvider;
import com.atomz.sawonz.global.util.CookieUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenPair login(LoginRequest loginRequest) {

        UsersEntity user = usersRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ErrorException(ResponseCode.WRONG_EMAIL_PASSWORD));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new ErrorException(ResponseCode.WRONG_EMAIL_PASSWORD);
        }

        if (user.getStatus() == null ) {
            throw new ErrorException(ResponseCode.LOGIN_NO_AUTHORIZATION);
        }
        if (!user.getStatus()) {
            throw new ErrorException(ResponseCode.SIGNUP_REJECTED);
        }

        String role = user.getRole().name();

        String at = jwtTokenProvider.generateAccessToken(user.getEmail(), role, user.getUserName());
        String rt = jwtTokenProvider.generateRefreshToken(user.getEmail(), role, user.getUserName());

        return TokenPair.of(at, rt, user.getEmail(), role);
    }

    public MeResponse me(CustomUserPrincipal principal) {

        return MeResponse.fromPrincipal(principal);
    }

    public TokenPair refresh(HttpServletRequest request) {

        String rt = CookieUtil.getCookieValue(request, "RT");
        if (rt == null || rt.isBlank()) {
            throw new ErrorException(ResponseCode.TOKEN_INVALID);
        }

        Claims claims;
        try {
            claims = jwtTokenProvider.parseClaims(rt); // 유효성 + 만료 확인
        } catch (ExpiredJwtException ex) {
            throw new ErrorException(ResponseCode.TOKEN_EXPIRED);
        } catch (Exception ex) {
            throw new ErrorException(ResponseCode.TOKEN_INVALID);
        }

        String email = claims.getSubject();
        String role = claims.get("role", String.class);
        String userName = claims.get("userName", String.class);

        // RT가 유효하면 AT 새로 발급
        String newAT = jwtTokenProvider.generateAccessToken(email, role, userName);

        return TokenPair.of(newAT, null, email, role);
    }
}
