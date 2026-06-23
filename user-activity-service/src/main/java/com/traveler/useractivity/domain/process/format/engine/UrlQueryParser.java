package com.traveler.useractivity.domain.process.format.engine;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UrlQueryParser {

    /**
     * URL 경로(path)나 Referer에서 쿼리 스트링(?key=value)을 추출하여 Map으로 반환합니다.
     */
    public Map<String, String> extractQueryParams(String url) {
        Map<String, String> queryParams = new HashMap<>();

        if (url == null || !url.contains("?")) {
            return queryParams;
        }

        try {
            // "?" 이후의 쿼리 스트링만 추출
            String queryString = url.substring(url.indexOf("?") + 1);
            String[] pairs = queryString.split("&");

            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                // 값이 없는 경우(예: "?action=") 빈 문자열 처리
                String value = keyValue.length > 1 && keyValue[1] != null
                        ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                        : "";

                queryParams.put(key, value);
            }
        } catch (Exception e) {
            log.warn("URL 쿼리 파라미터 파싱 중 오류 발생. URL: {}", url, e);
        }

        return queryParams;
    }
}
