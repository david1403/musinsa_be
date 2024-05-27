package com.musinsa.web;

import com.musinsa.common.ErrorCode;
import com.musinsa.common.MusinsaApiException;
import com.musinsa.model.dto.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(MusinsaApiException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse musinsaApiException(MusinsaApiException exception) {
        String errorCode = exception.getErrorCode().name();
        String errorMessage = messageSource.getMessage(exception.getErrorCode().getMessageKey(), null, null);

        return new ErrorResponse(errorCode, errorMessage);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse runtimeException() {
        String errorCode = ErrorCode.FAIL.name();
        String errorMessage = messageSource.getMessage(ErrorCode.FAIL.getMessageKey(), null, null);

        return new ErrorResponse(errorCode, errorMessage);
    }
}
