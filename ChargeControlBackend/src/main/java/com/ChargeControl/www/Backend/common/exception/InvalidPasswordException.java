package com.ChargeControl.www.Backend.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends BaseException {
    public InvalidPasswordException() {
        super(HttpStatus.BAD_REQUEST, "잘못된 비밀번호 입니다.");
    }
}
