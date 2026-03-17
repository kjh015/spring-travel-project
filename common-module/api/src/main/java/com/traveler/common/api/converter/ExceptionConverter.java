package com.traveler.common.api.converter;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.traveler.common.core.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ExceptionConverter {

    /**
     * @Valid 또는 @Validated 사용 시 DTO 바인딩 에러 처리
     */
    public List<ErrorResponse> from(BindException ex) {
        return Stream.concat(
                        ex.getBindingResult().getFieldErrors().stream()
                                .map(error -> ErrorResponse.builder()
                                        .field(error.getField())
                                        .value(error.getRejectedValue() == null ? "" : error.getRejectedValue())
                                        .reason(error.getDefaultMessage())
                                        .build()),
                        ex.getBindingResult().getGlobalErrors().stream()
                                .map(error -> ErrorResponse.builder()
                                        .field(error.getObjectName())
                                        .value("")
                                        .reason(error.getDefaultMessage())
                                        .build()))
                .toList();
    }

    /**
     * PathVariable이나 QueryParameter에서 발생한 제약 조건 위반 처리
     */
    public List<ErrorResponse> from(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(violation -> {
                    String path = violation.getPropertyPath().toString();
                    String field = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
                    return ErrorResponse.builder()
                            .field(field)
                            .value(violation.getInvalidValue())
                            .reason(violation.getMessage())
                            .build();
                })
                .toList();
    }

    /**
     * 컨트롤러 메서드 파라미터 타입이 일치하지 않을 때 처리 (ex: Integer 자리에 String 입력)
     */
    public List<ErrorResponse> from(MethodArgumentTypeMismatchException ex) {
        Class<?> requiredType = ex.getRequiredType();

        String message = getErrorMessage(requiredType);

        return List.of(ErrorResponse.builder()
                .field(ex.getName())
                .value(ex.getValue())
                .reason(message)
                .build());
    }

    /**
     * JSON 파싱 과정에서 포맷이 맞지 않거나 Enum 값이 잘못된 경우 처리
     */
    public List<ErrorResponse> from(InvalidFormatException ife) {
        String fieldPath = ife.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));

        String message = getErrorMessage(ife.getTargetType());

        return List.of(ErrorResponse.builder()
                .field(fieldPath)
                .value(ife.getValue())
                .reason(message)
                .build());
    }

    /**
     * 일반적인 문자열 메시지
     */
    public List<ErrorResponse> from(String reason) {
        return List.of(ErrorResponse.builder().field("").value("").reason(reason).build());
    }

    // --------- Util ---------
    /**
     * 타겟 타입에 따른 에러 메시지 생성 (Enum 특화)
     */
    private String getErrorMessage(Class<?> targetType) {
        if (targetType != null && targetType.isEnum()) {
            return String.format("잘못된 형식입니다. 허용값=[%s]", getAllowedEnumValues(targetType));
        }
        return "잘못된 형식의 값입니다.";
    }

    /**
     * Enum 클래스의 모든 상수 값을 콤마로 연결하여 반환
     */
    private String getAllowedEnumValues(Class<?> enumType) {
        return Arrays.stream(enumType.getEnumConstants()).map(Object::toString).collect(Collectors.joining(", "));
    }
}
