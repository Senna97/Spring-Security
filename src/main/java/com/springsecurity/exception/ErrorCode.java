package com.springsecurity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "Username is duplicated."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Username Not found"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Invalid password");

    private HttpStatus status;
    private String message;
}
