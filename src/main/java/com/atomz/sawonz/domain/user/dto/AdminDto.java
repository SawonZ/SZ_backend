package com.atomz.sawonz.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AdminDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserStatusRequest {

        @Email
        private String email;

        @NotNull
        private Boolean status;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserInfoRequest {

        @NotBlank(message = "이메일 값은 필수입니다.")
        @Email
        private String email;

        private Integer salary;
        private Integer annualLeaveCount;
        private String positionTitle;
        private LocalDate hiredAt;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserResignRequest {

        @NotBlank(message = "이메일 값은 필수입니다.")
        @Email
        private String email;

        @NotNull(message = "퇴직처리 여부 값은 필수입니다.")
        private Boolean resigned;

        private LocalDate resignedAt;

    }
}
