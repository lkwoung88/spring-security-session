package com.spring.code;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS("SUCCESS", "응답에 성공했습니다."),
    LOGIN_SUCCESS("LOGIN_SUCCESS", "로그인에 성공하였습니다."),
    LOGOUT_SUCCESS("LOGOUT_SUCCESS", "로그아웃에 성공하였습니다."),
    LOGIN_FAILURE("LOGIN_FAILURE", "로그인에 실패하였습니다."),
    NO_AUTHORIZATION("NO_AUTHORIZATION", "인증에 실패하였습니다."),
    NO_PERMISSION("NO_PERMISSION", "권한이 없습니다.")
    ;

    private String status;
    private String message;

    ResponseCode(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
