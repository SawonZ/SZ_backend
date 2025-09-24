package com.atomz.sawonz.domain.user.dto;

import com.atomz.sawonz.domain.user.entity.EmailCheckEntity;
import jakarta.validation.constraints.Email;
import lombok.*;

public class EmailCheckDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EmailSendRequest {

        @Email
        private String email;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class VerificationCodeSave {

        private String email;
        private String verificationCode;

        public static EmailCheckEntity toEntity(String email, String code) {
            return EmailCheckEntity.builder()
                    .email(email)
                    .verificationCode(code)
                    .verified(false)
                    .build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class VerificationCodeCheckRequest {

        @Email
        private String email;
        private String verificationCode;

    }

}
