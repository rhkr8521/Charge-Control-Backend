package com.ChargeControl.www.Backend.common.exception;

import org.springframework.http.HttpStatus;

public class VerificationException extends BaseException {

    // 기본 메시지를 사용하는 생성자
    public VerificationException() {
        super(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
    }

    // 외부에서 메시지를 전달받는 생성자
    public VerificationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
