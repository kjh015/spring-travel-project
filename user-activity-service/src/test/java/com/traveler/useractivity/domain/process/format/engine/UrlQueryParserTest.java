package com.traveler.useractivity.domain.process.format.engine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UrlQueryParserTest {

    private final UrlQueryParser parser = new UrlQueryParser();

    @Test
    @DisplayName("percent-encoding된 한글 쿼리 값을 디코딩하여 반환한다")
    void decodesEncodedKoreanValue() {
        Map<String, String> result = parser.extractQueryParams(
                "https://tripnow.com/board?title=%EA%B2%BD%EC%A3%BC%20%ED%99%A9%EB%A6%AC%EB%8B%A8%EA%B8%B8%20%EB%A7%9B%EC%A7%91%20%ED%88%AC%EC%96%B42");

        assertThat(result).containsEntry("title", "경주 황리단길 맛집 투어2");
    }

    @Test
    @DisplayName("영문/숫자 값과 다중 파라미터를 그대로 파싱한다")
    void parsesPlainValues() {
        Map<String, String> result = parser.extractQueryParams("/search?keyword=food&page=2");

        assertThat(result).containsEntry("keyword", "food").containsEntry("page", "2");
    }

    @Test
    @DisplayName("쿼리 스트링이 없으면 빈 Map을 반환한다")
    void returnsEmptyMapWhenNoQuery() {
        assertThat(parser.extractQueryParams("https://tripnow.com/board")).isEmpty();
        assertThat(parser.extractQueryParams(null)).isEmpty();
    }

    @Test
    @DisplayName("잘못된 인코딩 형식의 파라미터는 저장하지 않는다")
    void skipsInvalidEncoding() {
        assertThat(parser.extractQueryParams("/search?keyword=%ZZ")).isEmpty();
    }

    @Test
    @DisplayName("'+'는 공백으로 디코딩한다")
    void decodesPlusAsSpace() {
        Map<String, String> result = parser.extractQueryParams("/search?keyword=hot+place");

        assertThat(result).containsEntry("keyword", "hot place");
    }
}
