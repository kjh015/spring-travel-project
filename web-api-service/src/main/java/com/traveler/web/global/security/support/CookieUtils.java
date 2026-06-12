package com.traveler.web.global.security.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.web.global.exception.WebApiServiceException;
import com.traveler.web.global.exception.code.WebApiServiceErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {
    private final boolean secureCookie;
    private final ObjectMapper objectMapper;

    // ObjectMapper를 주입받아 사용합니다.
    public CookieUtils(@Value("${app.cookie.secure:true}") boolean secureCookie, ObjectMapper objectMapper) {
        this.secureCookie = secureCookie;
        this.objectMapper = objectMapper;
    }

    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(true)
                .secure(secureCookie)
                .sameSite("Lax") // OAuth2 리다이렉트를 위해 최소 Lax 이상 필요
                .maxAge(maxAge)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    ResponseCookie deleteCookie =
                            ResponseCookie.from(name, "").path("/").maxAge(0).build();
                    response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
                }
            }
        }
    }

    public String serialize(Object object) {
        try {
            // 객체를 JSON 문자열로 변환 후 Base64로 인코딩합니다.
            String json = objectMapper.writeValueAsString(object);
            return Base64.getUrlEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new WebApiServiceException(WebApiServiceErrorCode.COOKIE_SERIALIZE_ERROR);
        }
    }

    public <T> T deserialize(Cookie cookie, Class<T> cls) {
        try {
            // Base64 디코딩 후 JSON 문자열을 지정된 타입의 객체로 변환합니다.
            byte[] decodedBytes = Base64.getUrlDecoder().decode(cookie.getValue());
            String json = new String(decodedBytes, StandardCharsets.UTF_8);
            return objectMapper.readValue(json, cls);
        } catch (Exception e) {
            throw new WebApiServiceException(WebApiServiceErrorCode.COOKIE_DESERIALIZE_ERROR);
        }
    }
}
