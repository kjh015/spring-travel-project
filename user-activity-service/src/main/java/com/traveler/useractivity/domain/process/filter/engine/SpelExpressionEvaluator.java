package com.traveler.useractivity.domain.process.filter.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
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

        try {
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

        } catch (ParseException e) {
            // SpEL 문법 자체가 잘못된 경우
            log.error("SpEL 문법 파싱 오류. 표현식: {}, 원인: {}", expressionString, e.getMessage());
            return false;
        } catch (EvaluationException e) {
            // 로그 데이터 타입이 안 맞거나 필드가 없는 경우
            log.debug(
                    "SpEL 평가 중 데이터 불일치 발생. 평가실패 처리됨. 표현식: {}, 변수: {}, 원인: {}",
                    expressionString,
                    variables,
                    e.getMessage());
            return false;
        } catch (Exception e) {
            // 기타 알 수 없는 치명적 에러 방어
            log.error("SpEL 엔진 실행 중 예기치 않은 오류 발생. 표현식: {}", expressionString, e);
            return false;
        }
    }
}
