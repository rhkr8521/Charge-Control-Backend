package com.ChargeControl.www.Backend.common.exception;

import org.springframework.http.HttpStatus;

public class TokenNotFoundException extends BaseException {
    public TokenNotFoundException() {
        super(HttpStatus.BAD_REQUEST, "토큰값이 DB에 없습니다.");
    }
}