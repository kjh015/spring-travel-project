package com.traveler.web.global.feign.decoder;

import com.traveler.common.core.response.ApiResponse;
import com.traveler.web.global.code.WebApiServiceErrorCode;
import com.traveler.web.global.exception.WebApiServiceException;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ValidatedFeignDecoder implements Decoder {
    private final Decoder delegate;

    public ValidatedFeignDecoder(Decoder delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        Object decodedObject = delegate.decode(response, type);

        if (decodedObject instanceof ApiResponse<?> apiResponse) {
            // 1. 반환 타입이 ApiResponse<Void>인지 확인
            if (isVoidType(type)) {
                return decodedObject; // Void면 null 체크 없이 즉시 반환
            }

            // 2. Void가 아님에도 result가 null이면 예외 발생
            if (apiResponse.result() == null) {
                throw new WebApiServiceException(WebApiServiceErrorCode.SERVICE_RESPONSE_EMPTY);
            }
        }
        return decodedObject;
    }

    private boolean isVoidType(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            Type innerType = parameterizedType.getActualTypeArguments()[0];
            return innerType.equals(Void.class) || innerType.equals(void.class);
        }
        return false;
    }
}
