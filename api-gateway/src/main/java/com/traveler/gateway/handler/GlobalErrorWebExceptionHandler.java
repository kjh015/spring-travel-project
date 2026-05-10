package com.traveler.gateway.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.common.core.code.BaseErrorCode;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.gateway.exception.ApiGatewayErrorCode;
import com.traveler.gateway.exception.ApiGatewayException;
import com.traveler.gateway.exception.ApiGatewayNoStackException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-1)
@RequiredArgsConstructor
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }

        BaseErrorCode errorCode = determineErrorCode(ex);

        return logError(exchange, ex, errorCode).then(Mono.defer(() -> renderErrorResponse(exchange, errorCode)));
    }

    private BaseErrorCode determineErrorCode(Throwable ex) {
        if (ex instanceof ApiGatewayException age) {
            return age.getCode();
        }
        if (ex instanceof ApiGatewayNoStackException agne) {
            return agne.getCode();
        }
        if (ex instanceof NoResourceFoundException) {
            return ApiGatewayErrorCode.ROUTE_NOT_FOUND;
        }
        return ErrorCode.INTERNAL_SERVER_ERROR;
    }

    private Mono<Void> logError(ServerWebExchange exchange, Throwable ex, BaseErrorCode code) {
        // Mono.fromRunnable을 통해 리액티브 흐름 내에서 로깅 수행
        return Mono.fromRunnable(() -> {
            String path = exchange.getRequest().getPath().toString();
            if (ex instanceof ApiGatewayNoStackException) {
                // 비즈니스 예외: WARN 레벨, 스택 트레이스 제외하여 성능 최적화 및 알람 방지
                log.warn(
                        "[Gateway Business Error] Path: {}, Code: {}, Message: {}",
                        path,
                        code.getCode(),
                        ex.getMessage());
            } else {
                // 시스템 예외: ERROR 레벨, 원인 파악을 위한 전체 스택 트레이스 포함
                log.error(
                        "[Gateway System Error] Path: {}, Code: {}, Message: {}",
                        path,
                        code.getCode(),
                        ex.getMessage(),
                        ex);
            }
        });
    }

    private Mono<Void> renderErrorResponse(ServerWebExchange exchange, BaseErrorCode errorCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.valueOf(errorCode.getStatus()));

        ApiResponse<Void> errorBody = ApiResponse.onFailure(errorCode, null);

        return response.writeWith(Mono.fromCallable(() -> {
                    DataBufferFactory bufferFactory = response.bufferFactory();
                    byte[] bytes = objectMapper.writeValueAsBytes(errorBody);
                    return bufferFactory.wrap(bytes);
                })
                .doOnError(e -> log.error("Error writing response", e)));
    }
}
