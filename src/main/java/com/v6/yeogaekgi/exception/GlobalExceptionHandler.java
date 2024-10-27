package com.v6.yeogaekgi.exception;

import java.util.NoSuchElementException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    // 외의 모든 에러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage());
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_ERROR", "Internal server error occurred");
    }

    // 400 잘못된 요청 데이터
    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotWritableException(HttpMessageNotWritableException e) {
        log.error(e.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_REQUEST_DATA", "잘못된 요청 데이터입니다");
    }

    // 400 JSON 파싱 실패 시
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseException(HttpMessageNotReadableException e) {
        // JSON 형식이 잘못되었을 때
        log.error(e.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_JSON", "잘못된 JSON 형식");
    }

    // 400 타입 변환 실패 시
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        // 예: String을 Integer로 변환 실패 시
        log.error(e.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, "TYPE_MISMATCH", "잘못된 타입");
    }

    // 400 필수 파라미터 누락 시 발생
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParamException(MissingServletRequestParameterException e) {
        // 필수 파라미터가 없을 때
        log.error(e.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, "MISSING_PARAMETER", "필수 파라미터 누락");
    }

    // 400 잘못된 요청 데이터나 유효성 검증 실패 시
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, "MISSING_PARAMETER", "유효성 검증 실패");
    }

    // 401 인증 실패
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        log.error(e.getMessage());
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증에 실패했습니다");
    }

    // 403 권한 없음
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage());
        return createErrorResponse(HttpStatus.FORBIDDEN, "FORBIDDEN", "접근 권한이 없습니다");
    }

    // 404 리소스를 찾을 수 없음
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NoSuchElementException e) {
        log.error(e.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, "NOT_FOUND", "요청한 리소스를 찾을 수 없습니다");
    }

    // 405 허용되지 않는 메서드
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage());
        return createErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "지원하지 않는 HTTP 메서드입니다");
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String code, String message) {
        ErrorResponse response = ErrorResponse.builder()
                .code(code)
                .message(message)
                .status(status.value())
                .build();
        return new ResponseEntity<>(response, status);
    }

}
