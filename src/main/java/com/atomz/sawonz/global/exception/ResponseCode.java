package com.atomz.sawonz.global.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /**
     * Internal Server Error : 500
     * UNAUTHORIZED : 401
     * FORBIDDEN : 403
     * NOT_FOUND : 404
     * BAD_REQUEST : 400
     */

    /**
     * 공통 성공 코드
     */
    // 200
    SUCCESS(HttpStatus.OK, "요청 성공"),

    /**
     * 공통 에러 코드
     */
    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
    EMAIL_VERIFICATION_FAILED(HttpStatus.BAD_REQUEST, "검증 되지 않은 이메일의 요청입니다."),

    // 401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "토큰이 없습니다"),
    WRONG_EMAIL_PASSWORD(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    TOKEN_INVALID(org.springframework.http.HttpStatus.UNAUTHORIZED, "토큰이 없거나, 유효하지 않은 토큰입니다"),
    TOKEN_EXPIRED(org.springframework.http.HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다"),
    TOKEN_SUBJECT_MISSING(org.springframework.http.HttpStatus.UNAUTHORIZED, "토큰에 subject 정보가 없습니다"),
    TOKEN_ROLE_MISSING(org.springframework.http.HttpStatus.UNAUTHORIZED, "토큰에 role 정보가 없습니다"),
    NOT_ADMIN_MANAGER(HttpStatus.UNAUTHORIZED, "요청권한이 없습니다."),
    LOGIN_NO_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "관리자 승인 대기중입니다."),

    // 403
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근권한이 없습니다"),
    SIGNUP_REJECTED(HttpStatus.FORBIDDEN, "회원가입 요청이 거절되었습니다."),

    // 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청값을 찾을 수 없습니다"),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "요청한 사용자의 정보가 없습니다."),

    // 409
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다"),
    PHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 연락처입니다"),

    // 410 GONE
    VERIFICATION_CODE_EXPIRED(HttpStatus.GONE, "인증번호 유효시간(3분)이 만료되었습니다"),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다");

    private final HttpStatus status;
    private final String description;
}
