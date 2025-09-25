package com.atomz.sawonz.domain.user.dto;

import com.atomz.sawonz.global.security.CustomUserPrincipal;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

public class AuthDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LoginRequest {

        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class TokenPair {
        private final String accessToken;
        private final String refreshToken;
        private final String email;
        private final String roles;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MeResponse {

        private String email;
        private String userName;
        private String role;

        public static MeResponse fromPrincipal(CustomUserPrincipal principal) {
            return MeResponse.builder()
                    .email(principal.getEmail())
                    .userName(principal.getUsername())
                    .role(principal.getRole())
                    .build();
        }
    }


}
