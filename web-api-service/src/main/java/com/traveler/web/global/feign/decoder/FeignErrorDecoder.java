package com.traveler.web.global.feign.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.common.core.code.BaseErrorCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.web.global.code.WebApiServiceErrorCode;
import com.traveler.web.global.exception.WebApiServiceException;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.body() == null) {
            log.error("[Feign Error] Response body is null. Status: {}", response.status());
            return new WebApiServiceException(WebApiServiceErrorCode.SERVICE_RESPONSE_EMPTY);
        }

        try (InputStream bodyIs = response.body().asInputStream()) {
            // post-service의 ApiResponse<Void> 에러 응답 파싱
            ApiResponse<?> apiResponse = objectMapper.readValue(bodyIs, ApiResponse.class);

            BaseErrorCode serviceErrorCode =
                    ServiceErrorCode.of(response.status(), apiResponse.getCode(), apiResponse.getMessage());

            log.warn(
                    "[Feign Error] Method: {}, Status: {}, Code: {}, Message: {}",
                    methodKey,
                    response.status(),
                    serviceErrorCode.getCode(),
                    serviceErrorCode.getMessage());

            return new WebApiServiceException(serviceErrorCode);
        } catch (Exception e) {
            log.error(
                    "[Feign Error] Failed to decode error response. Method: {}, Status: {}",
                    methodKey,
                    response.status(),
                    e);
            return new WebApiServiceException(WebApiServiceErrorCode.SERVICE_PARSE_ERROR);
        }
    }
}
