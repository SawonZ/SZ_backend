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

    // 403
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근권한이 없습니다"),

    // 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청값을 찾을 수 없습니다"),

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
