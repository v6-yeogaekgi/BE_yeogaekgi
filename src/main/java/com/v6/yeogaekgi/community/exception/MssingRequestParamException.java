package com.v6.yeogaekgi.community.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MssingRequestParamException extends RuntimeException {
    public MssingRequestParamException(String message) {
        super(message);
    }
}