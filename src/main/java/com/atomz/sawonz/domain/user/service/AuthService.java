package com.atomz.sawonz.domain.user.service;

import com.atomz.sawonz.domain.user.dto.AuthDto.LoginRequest;
import com.atomz.sawonz.domain.user.dto.AuthDto.TokenPair;
import com.atomz.sawonz.domain.user.entity.UsersEntity;
import com.atomz.sawonz.domain.user.repository.UsersRepository;
import com.atomz.sawonz.global.exception.ErrorException;
import com.atomz.sawonz.global.exception.ResponseCode;
import com.atomz.sawonz.global.security.JwtTokenProvider;
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

        if (!Boolean.TRUE.equals(user.getStatus())) {
            // status 기본값이 false(비활성)인 구조이므로, 승인 전에는 로그인 차단
            throw new ErrorException(ResponseCode.NO_AUTHORIZATION);
        }

        String role = user.getRole().name();

        String at = jwtTokenProvider.generateAccessToken(user.getEmail(), role);
        String rt = jwtTokenProvider.generateRefreshToken(user.getEmail(), role);

        return new TokenPair(at, rt, user.getEmail(), role);
    }
}
