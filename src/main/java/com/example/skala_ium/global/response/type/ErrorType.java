package com.example.skala_ium.global.response.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    // common
    ENUM_RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이넘 값을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "형식에 맞지 않는 요청입니다."),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "요청한 정보로 엔터티를 찾을 수 없습니다."),

    // auth
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "아이디와 비밀번호가 틀렸습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 엑세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 리프레쉬 토큰입니다."),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효한 토큰이 아닙니다."),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 역할입니다."),

    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),

    // assignment
    ASSIGNMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 과제를 찾을 수 없습니다."),

    // course
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 강의를 찾을 수 없습니다."),
    NOT_ENROLLED(HttpStatus.FORBIDDEN, "해당 강의에 등록되지 않은 학생입니다."),

    // submission
    SUBMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 제출을 찾을 수 없습니다."),
    ALREADY_SUBMITTED(HttpStatus.CONFLICT, "이미 제출한 과제입니다."),
    DEADLINE_PASSED(HttpStatus.BAD_REQUEST, "과제 마감일이 지났습니다."),

    // verification
    VERIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "검증 결과를 찾을 수 없습니다."),
    AI_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AI 서비스 호출에 실패했습니다."),
    ;

    private final HttpStatus httpStatusCode;
    private final String message;
}
