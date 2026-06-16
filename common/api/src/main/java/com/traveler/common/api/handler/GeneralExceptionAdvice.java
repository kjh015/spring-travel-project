package com.traveler.common.api.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.traveler.common.api.converter.ExceptionConverter;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.exception.GeneralException;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
public class GeneralExceptionAdvice implements BaseExceptionAdvice {

    private final ExceptionConverter exceptionConverter;

    /** [Business Exception] 커스텀 비즈니스 로직 예외 처리 */
    @ExceptionHandler(GeneralException.class)
    protected ResponseEntity<ApiResponse<Void>> handleGeneralException(GeneralException ex) {
        if (ex.getCause() != null) {
            log.error("[System Exception] Code: {}, Message: {}", ex.getCode(), ex.getMessage(), ex);
        } else {
            log.warn("[Business Exception] Code: {}, Message: {}", ex.getCode(), ex.getMessage());
        }
        return createErrorResponse(ex.getCode(), null);
    }

    /** [Validation Exception] @RequestBody, @ModelAttribute 객체의 @Valid 검증 실패 */
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    protected ResponseEntity<ApiResponse<List<ErrorResponse>>> handleBindException(BindException ex) {
        List<ErrorResponse> errors = exceptionConverter.from(ex);
        log.info(
                "[Validation Error] Count: {}, Fields: {}",
                errors.size(),
                errors.stream().map(ErrorResponse::field).toList());
        return createErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
    }

    /** [Validation Exception] @Validated가 선언된 클래스 내 @RequestParam, @PathVariable 제약 조건 위반 */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiResponse<List<ErrorResponse>>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        List<ErrorResponse> errors = exceptionConverter.from(ex);
        log.info(
                "[Constraint Violation] Fields: {}",
                errors.stream().map(ErrorResponse::field).toList());
        return createErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
    }

    /** [Type Mismatch] @RequestParam, @PathVariable 요청 값의 타입이 파라미터 타입과 불일치 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiResponse<List<ErrorResponse>>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        List<ErrorResponse> errors = exceptionConverter.from(ex);
        log.info("[Method Argument Type Mismatch] Field: {}, Value: {}", ex.getName(), ex.getValue());
        return createErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
    }

    /** [JSON Parsing Error] @RequestBody 데이터 파싱 실패 또는 Enum 타입 불일치 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiResponse<List<ErrorResponse>>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException ife) {
            List<ErrorResponse> errors = exceptionConverter.from(ife);
            String field = errors.isEmpty() ? "unknown" : errors.getFirst().field();
            log.info("[JSON Format Error] Field: {}", field);
            return createErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
        }
        log.warn("[Message Not Readable] CauseType: {}", ex.getClass().getSimpleName());
        return createErrorResponse(ErrorCode.INVALID_TYPE_VALUE, exceptionConverter.from("요청 JSON 형식이 올바르지 않습니다."));
    }

    /** [404 Error] 존재하지 않는 URL 호출 (404) */
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(
            NoResourceFoundException ex, HttpServletRequest request) {
        log.warn("[404 Not Found] Path: {}", request.getRequestURI());
        return createErrorResponse(ErrorCode.NOT_FOUND, null);
    }

    /** [405 Error] 지원하지 않는 HTTP 메서드 호출 (405) */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiResponse<Void>> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.warn("[405 Method Not Allowed] Method: {}, Path: {}", request.getMethod(), request.getRequestURI());
        return createErrorResponse(ErrorCode.METHOD_NOT_ALLOWED, null);
    }

    /** [Missing Parameter] @RequestParam(required = true) 설정된 필수 쿼리 파라미터 누락 */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ApiResponse<List<ErrorResponse>>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        log.info("[Missing Parameter] Name: {}, Type: {}", ex.getParameterName(), ex.getParameterType());
        return createErrorResponse(
                ErrorCode.BAD_REQUEST, exceptionConverter.from(ex.getParameterName() + " 파라미터가 누락되었습니다."));
    }

    /** [415 Error] 지원하지 않는 Media Type(Content-Type)으로 요청 (415) */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ApiResponse<Void>> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex) {
        log.warn("[Unsupported Media Type] Given: {}, Supported: {}", ex.getContentType(), ex.getSupportedMediaTypes());
        return createErrorResponse(ErrorCode.UNSUPPORTED_MEDIA_TYPE, null);
    }

    /** [Illegal Argument] 메서드에 부적절한 인자가 전달되었을 때 (400) */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ApiResponse<List<ErrorResponse>>> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        log.warn("[Illegal Argument] Message: {}", ex.getMessage());
        // 예외 메시지를 그대로 클라이언트에게 전달하거나, 공통 에러 코드로 변환하여 응답
        return createErrorResponse(ErrorCode.BAD_REQUEST, exceptionConverter.from(ex.getMessage()));
    }

    /** [500 Internal Server Error] 사전에 정의되지 않은 모든 예외 처리 */
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
}
