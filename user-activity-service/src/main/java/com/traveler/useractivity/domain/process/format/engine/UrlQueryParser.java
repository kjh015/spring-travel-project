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

        String queryString = url.substring(url.indexOf('?') + 1);
        String[] pairs = queryString.split("&");

        for (String pair : pairs) {
            // "&&" 처럼 비어있는 엣지 케이스 무시
            if (pair.isEmpty()) continue;

            try {
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

                // 동일 키가 여러 번 오면 첫 번째 값을 유지한다 (기존 파서 동작과 동일)
                queryParams.putIfAbsent(key, value);
            } catch (IllegalArgumentException e) {
                // 잘못된 인코딩 항목 하나 때문에 나머지 파라미터 파싱을 중단하지 않는다
                log.warn("URL 쿼리 파라미터 디코딩 실패로 해당 항목 무시. pair: {}, 원인: {}", pair, e.getMessage());
            }
        }

        return queryParams;
    }
}
