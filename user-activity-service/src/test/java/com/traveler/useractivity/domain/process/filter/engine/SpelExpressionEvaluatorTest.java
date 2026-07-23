package com.traveler.useractivity.domain.process.filter.engine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SpelExpressionEvaluatorTest {

    @Test
    @DisplayName("숫자 문자열 값은 숫자로 변환되어 정수 비교가 가능하다 (result-count-sanity 규칙)")
    void numericStringComparedAsNumber() {
        Map<String, String> variables = Map.of("resultCount", "-1");

        boolean result = SpelExpressionEvaluator.evaluate("(#resultCount != null && #resultCount >= -1)", variables);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("복합 조건식도 숫자 비교가 가능하다 (stay-seconds-sanity 규칙)")
    void compoundNumericCondition() {
        Map<String, String> variables = Map.of("postId", "-1", "staySeconds", "-1");

        boolean result = SpelExpressionEvaluator.evaluate(
                "(#staySeconds != null && #staySeconds >= -1) && (#staySeconds != null && #staySeconds < 86400)",
                variables);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("조건 범위를 벗어난 숫자 값은 false를 반환한다")
    void numericConditionFails() {
        Map<String, String> variables = Map.of("staySeconds", "86400");

        boolean result = SpelExpressionEvaluator.evaluate(
                "(#staySeconds != null && #staySeconds >= -1) && (#staySeconds != null && #staySeconds < 86400)",
                variables);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("소수 문자열 값은 실수로 변환되어 비교가 가능하다")
    void decimalStringComparedAsNumber() {
        Map<String, String> variables = Map.of("score", "3.5");

        boolean result = SpelExpressionEvaluator.evaluate("#score > 3.0", variables);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("불리언 문자열 값은 불리언으로 변환된다")
    void booleanStringComparedAsBoolean() {
        Map<String, String> variables = Map.of("success", "true");

        boolean result = SpelExpressionEvaluator.evaluate("#success == true", variables);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("일반 문자열 값은 문자열 그대로 비교된다")
    void plainStringComparedAsString() {
        Map<String, String> variables = Map.of("keyword", "제주도");

        boolean result = SpelExpressionEvaluator.evaluate("#keyword == '제주도'", variables);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Long 범위를 벗어나는 숫자 문자열은 문자열로 유지된다")
    void overflowNumericStringKeptAsString() {
        Map<String, String> variables = Map.of("bigValue", "99999999999999999999999999");

        boolean result = SpelExpressionEvaluator.evaluate("#bigValue == '99999999999999999999999999'", variables);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("null 값이 포함된 변수는 null 체크 조건에서 false로 평가된다")
    void nullValueHandled() {
        Map<String, String> variables = new HashMap<>();
        variables.put("resultCount", null);

        boolean result = SpelExpressionEvaluator.evaluate("(#resultCount != null && #resultCount >= -1)", variables);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("빈 표현식은 false를 반환한다")
    void blankExpressionReturnsFalse() {
        assertThat(SpelExpressionEvaluator.evaluate("", Map.of())).isFalse();
        assertThat(SpelExpressionEvaluator.evaluate(null, Map.of())).isFalse();
    }
}
