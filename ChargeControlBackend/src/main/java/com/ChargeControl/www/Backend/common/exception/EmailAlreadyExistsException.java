package com.ChargeControl.www.Backend.common.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException() {
        super(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");
    }
}
