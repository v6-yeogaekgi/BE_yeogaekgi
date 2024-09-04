package com.v6.yeogaekgi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor

public class ErrorDetails {
    private final LocalDate timestamp;
    private final String message;
    private final String details;

}