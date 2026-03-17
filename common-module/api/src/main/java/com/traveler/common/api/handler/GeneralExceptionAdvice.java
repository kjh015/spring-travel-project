package com.traveler.common.api.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.traveler.common.api.converter.ExceptionConverter;
import com.traveler.common.core.code.BaseErrorCode;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.exception.GeneralException;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GeneralExceptionAdvice {

    private final ExceptionConverter exceptionConverter;

    /**
     * [Business Exception] 커스텀 비즈니스 로직 예외 처리
     */
    @ExceptionHandler(GeneralException.class)
    protected ResponseEntity<ApiResponse<Void>> handleGeneralException(GeneralException ex) {
        log.warn("[Business Exception] Code: {}, Message: {}", ex.getCode(), ex.getMessage());
        return createErrorResponse(ex.getCode(), null);
    }

    /**
     * [Validation Exception] @RequestBody, @ModelAttribute 객체의 @Valid 검증 실패
     */
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    protected ResponseEntity<ApiResponse<List<ErrorResponse>>> handleBindException(BindException ex) {
        List<ErrorResponse> errors = exceptionConverter.from(ex);
        log.info("[Validation Error] Count: {}, Details: {}", errors.size(), errors);
        return createErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
    }

    /**
     * [Validation Exception] @Validated가 선언된 클래스 내 @RequestParam, @PathVariable 제약 조건 위반
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiResponse<List<ErrorResponse>>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        List<ErrorResponse> errors = exceptionConverter.from(ex);
        log.info("[Constraint Violation] Details: {}", errors);
        return createErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
    }

    /**
     * [Type Mismatch] @RequestParam, @PathVariable 요청 값의 타입이 파라미터 타입과 불일치
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiResponse<List<ErrorResponse>>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        List<ErrorResponse> errors = exceptionConverter.from(ex);
        log.info("[Method Argument Type Mismatch] Field: {}, Value: {}", ex.getName(), ex.getValue());
        return createErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
    }

    /**
     * [JSON Parsing Error] @RequestBody 데이터 파싱 실패 또는 Enum 타입 불일치
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiResponse<List<ErrorResponse>>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException ife) {
            List<ErrorResponse> errors = exceptionConverter.from(ife);
            log.info(
                    "[JSON Format Error] Field: {}, Value: {}",
                    errors.get(0).field(),
                    errors.get(0).value());
            return createErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
        }
        log.warn("[Message Not Readable] Raw Message: {}", ex.getMessage());
        return createErrorResponse(ErrorCode.INVALID_TYPE_VALUE, exceptionConverter.from("요청 JSON 형식이 올바르지 않습니다."));
    }

    /**
     * [404 Error] 존재하지 않는 URL 호출 (404)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(
            NoResourceFoundException ex, HttpServletRequest request) {
        log.warn("[404 Not Found] Path: {}", request.getRequestURI());
        return createErrorResponse(ErrorCode.NOT_FOUND, null);
    }

    /**
     * [405 Error] 지원하지 않는 HTTP 메서드 호출 (405)
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiResponse<Void>> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.warn("[405 Method Not Allowed] Method: {}, Path: {}", request.getMethod(), request.getRequestURI());
        return createErrorResponse(ErrorCode.METHOD_NOT_ALLOWED, null);
    }

    /**
     * [Missing Parameter] @RequestParam(required = true) 설정된 필수 쿼리 파라미터 누락
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ApiResponse<List<ErrorResponse>>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        log.info("[Missing Parameter] Name: {}, Type: {}", ex.getParameterName(), ex.getParameterType());
        return createErrorResponse(
                ErrorCode.BAD_REQUEST, exceptionConverter.from(ex.getParameterName() + " 파라미터가 누락되었습니다."));
    }

    /**
     * [415 Error] 지원하지 않는 Media Type(Content-Type)으로 요청 (415)
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ApiResponse<Void>> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex) {
        log.warn("[Unsupported Media Type] Given: {}, Supported: {}", ex.getContentType(), ex.getSupportedMediaTypes());
        return createErrorResponse(ErrorCode.UNSUPPORTED_MEDIA_TYPE, null);
    }

    /**
     * 서버에 요청은 전달되었으나, 권한이 부족하여 거절된 경우 (403)
     * 주로 @PreAuthorize 등으로 권한 제어를 할 때 발생
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("[Access Denied] 권한 부족: {}", ex.getMessage());
        return createErrorResponse(ErrorCode.FORBIDDEN, null);
    }

    /**
     * [500 Internal Server Error] 사전에 정의되지 않은 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error(
                "[Uncaught Exception] Method: {}, URI: {}, Message: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage(),
                ex);
        return createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, null);
    }

    /**
     * 공통 응답 생성 메서드
     */
    private <T> ResponseEntity<ApiResponse<T>> createErrorResponse(BaseErrorCode code, T data) {
        return ResponseEntity.status(code.getStatus()).body(ApiResponse.onFailure(code, data));
    }
}
