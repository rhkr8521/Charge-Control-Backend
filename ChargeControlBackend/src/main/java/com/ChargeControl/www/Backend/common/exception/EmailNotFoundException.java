package com.ChargeControl.www.Backend.common.exception;

import org.springframework.http.HttpStatus;

public class EmailNotFoundException extends BaseException {
    public EmailNotFoundException() {
        super(HttpStatus.BAD_REQUEST, "가입되지 않은 이메일 입니다.");
    }
}
