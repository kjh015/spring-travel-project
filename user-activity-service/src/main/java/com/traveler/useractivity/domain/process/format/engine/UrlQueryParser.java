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
     * 값은 percent-encoding을 디코딩한 상태로 반환합니다.
     */
    public Map<String, String> extractQueryParams(String url) {
        Map<String, String> queryParams = new HashMap<>();

        if (url == null || !url.contains("?")) {
            return queryParams;
        }

        try {
            String queryString = url.substring(url.indexOf('?') + 1);
            String[] pairs = queryString.split("&");

            for (String pair : pairs) {
                // "&&" 처럼 비어있는 엣지 케이스 무시
                if (pair.isEmpty()) continue;

                int idx = pair.indexOf('=');

                String key;
                String value;

                if (idx != -1) {
                    // '=' 가 있는 경우
                    key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                    value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                } else {
                    // '=' 가 없는 경우 (예: ?flag) -> 버리지 않고 빈 문자열로 저장
                    key = URLDecoder.decode(pair, StandardCharsets.UTF_8);
                    value = "";
                }

                queryParams.put(key, value);
            }
        } catch (Exception e) {
            log.warn("URL 쿼리 파라미터 파싱 중 오류 발생. URL: {}", url, e);
        }

        return queryParams;
    }
}
