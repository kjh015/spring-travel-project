package com.traveler.web.global.exception;

import feign.RetryableException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 예외 검사 및 처리를 위한 공통 유틸리티 클래스
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionUtils {
    public static boolean isRetryableException(Exception e) {
        // 네트워크 레벨 장애
        if (e instanceof RetryableException || e instanceof SocketTimeoutException || e instanceof ConnectException) {
            return true;
        }

        // 하위 서비스의 일시적 서버 장애 (5xx)
        if (e instanceof WebApiServiceException webException) {
            int status = webException.getCode().getStatus();
            return status >= 500 && status < 600;
        }

        return false;
    }
}
