package com.ChargeControl.www.Backend.api.member.dto;

import lombok.Getter;

public class EmailRequest {
    @Getter
    public static class EmailForVerificationRequest {
        private String email;
    }

    @Getter
    public static class VerificationCodeRequest {
        private String code;
    }
}