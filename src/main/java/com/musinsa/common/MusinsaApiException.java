package com.musinsa.common;

import lombok.Getter;

@Getter
public class MusinsaApiException extends RuntimeException {
    private ErrorCode errorCode;

    public MusinsaApiException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }
}
