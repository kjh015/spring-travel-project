package com.traveler.useractivity.domain.process.filter.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

@Slf4j
public final class SpelExpressionEvaluator {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    // OOM 방지를 위해 최대 캐시 사이즈를 강제
    private static final int MAX_CACHE_SIZE = 10000;
    private static final Map<String, Expression> EXPRESSION_CACHE = new ConcurrentHashMap<>();

    private SpelExpressionEvaluator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * SpEL 표현식과 변수를 받아 조건의 참/거짓을 안전하게 평가합니다.
     */
    public static boolean evaluate(String expressionString, Map<String, String> variables) {
        if (expressionString == null || expressionString.isBlank()) {
            return false;
        }

        // 메모리 누수 방어 (캐시가 비정상적으로 커지면 초기화 - 임시 조치)
        if (EXPRESSION_CACHE.size() > MAX_CACHE_SIZE) {
            log.warn("SpEL 표현식 캐시가 최대치({})를 초과하여 초기화합니다.", MAX_CACHE_SIZE);
            EXPRESSION_CACHE.clear();
        }

        // 파싱 및 캐싱
        Expression expression = EXPRESSION_CACHE.computeIfAbsent(expressionString, PARSER::parseExpression);

        // 변수 바인딩
        EvaluationContext context =
                SimpleEvaluationContext.forReadOnlyDataBinding().build();
        if (variables != null && !variables.isEmpty()) {
            variables.forEach(context::setVariable);
        }

        // 평가 실행
        Boolean result = expression.getValue(context, Boolean.class);
        return Boolean.TRUE.equals(result);
    }
}
