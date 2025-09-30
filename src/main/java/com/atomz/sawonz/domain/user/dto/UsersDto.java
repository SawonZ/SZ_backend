package com.atomz.sawonz.domain.user.dto;

import com.atomz.sawonz.domain.user.entity.UsersEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.*;

public class UsersDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignupRequest {

        @NotBlank
        @Size(min = 2, max = 64)
        private String userName;

        @NotBlank
        @Size(min = 8, max = 64)
        private String password; // 평문 입력 → 서비스에서 BCrypt로 해시

        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 7, max = 20)
        @Pattern(regexp = "^[0-9\\-+]{7,20}$", message = "전화번호 형식이 올바르지 않습니다.")
        private String phone;

        public static UsersEntity toEntity(
                SignupRequest signupRequest,
                String passwordHash
                ) {

            return UsersEntity.builder()
                    .userName(signupRequest.getUserName())
                    .email(signupRequest.getEmail())
                    .phone(signupRequest.getPhone())
                    .passwordHash(passwordHash)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignupResponse {

        private String email;

        public static SignupResponse fromEntity(UsersEntity usersEntity) {
            return SignupResponse.builder()
                    .email(usersEntity.getEmail())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyInfoResponse {

        private String userName;
        private String email;
        private String phone;
        private String address;
        private Integer salary;
        private Integer annualLeaveCount;
        private String positionTitle;
        private Boolean status;
        private LocalDate hiredAt;
        private LocalDate resignedAt;

        public static MyInfoResponse fromEntity(UsersEntity usersEntity) {
            return MyInfoResponse.builder()
                    .userName(usersEntity.getUserName())
                    .email(usersEntity.getEmail())
                    .phone(usersEntity.getPhone())
                    .address(usersEntity.getUserPrivate().getAddress())
                    .salary(usersEntity.getUserPrivate().getSalary())
                    .annualLeaveCount(usersEntity.getUserPrivate().getAnnualLeaveCount())
                    .positionTitle(usersEntity.getUserPrivate().getPositionTitle())
                    .status(usersEntity.getStatus())
                    .hiredAt(usersEntity.getUserPrivate().getHiredAt())
                    .resignedAt(usersEntity.getUserPrivate().getResignedAt())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyInfoUpdateRequest {

        private String phone;
        private String address;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyCoworkerInfoResponse {

        private String userName;
        private String email;
        private String phone;
        private String positionTitle;

        public static MyCoworkerInfoResponse fromEntity(UsersEntity usersEntity) {
            return MyCoworkerInfoResponse.builder()
                    .userName(usersEntity.getUserName())
                    .email(usersEntity.getEmail())
                    .phone(usersEntity.getPhone())
                    .positionTitle(usersEntity.getUserPrivate().getPositionTitle())
                    .build();
        }
    }

}
