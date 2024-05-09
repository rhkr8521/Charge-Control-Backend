package com.ChargeControl.www.Backend.common.exception;

import org.springframework.http.HttpStatus;

public class MemberException extends BaseException {

    public MemberException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
