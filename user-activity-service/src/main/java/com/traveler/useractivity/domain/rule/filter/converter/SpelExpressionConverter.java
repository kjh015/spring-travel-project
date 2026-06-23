package com.traveler.useractivity.domain.rule.filter.converter;

import com.traveler.useractivity.domain.rule.filter.enums.ComparisonOperator;
import com.traveler.useractivity.domain.rule.filter.enums.ValueType;
import com.traveler.useractivity.domain.rule.filter.vo.FilterNode;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;
import java.util.List;
import java.util.regex.Pattern;

public final class SpelExpressionConverter {

    private static final String SPEL_PREFIX = "#";
    private static final String SPEL_EQUALS_FORMAT = "(%1$s%2$s != null && %1$s%2$s matches '(?i)%3$s')";
    private static final String SPEL_DEFAULT_FORMAT = "(%1$s%2$s != null && %1$s%2$s %3$s %4$s)";

    private SpelExpressionConverter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String convert(List<FilterNode.Element> conditions) {
        if (conditions == null || conditions.isEmpty()) return "";

        StringBuilder spel = new StringBuilder();

        for (FilterNode.Element element : conditions) {
            switch (element) {
                case FilterNode.Condition cond -> appendCondition(spel, cond);
                case FilterNode.Operator op -> spel.append(" ")
                        .append(op.value().getValue())
                        .append(" ");
                case FilterNode.Paren paren -> spel.append("left-paren".equals(paren.type()) ? "(" : ")");
                default -> throw new UserActivityServiceException(
                        UserActivityServiceErrorCode.FILTER_UNSUPPORTED_NODE_TYPE);
            }
        }
        return spel.toString();
    }

    private static void appendCondition(StringBuilder spel, FilterNode.Condition cond) {
        String safeValue = escapeSpelString(cond.value());

        if (cond.operator() == ComparisonOperator.EQUALS) {
            String regexSafeValue = Pattern.quote(safeValue);
            spel.append(SPEL_EQUALS_FORMAT.formatted(SPEL_PREFIX, cond.field(), regexSafeValue));
        } else {
            String formattedValue = formatValue(cond.valueType(), safeValue);
            spel.append(SPEL_DEFAULT_FORMAT.formatted(
                    SPEL_PREFIX, cond.field(), cond.operator().getValue(), formattedValue));
        }
    }

    private static String formatValue(ValueType valueType, String safeValue) {
        if (valueType == ValueType.STRING) {
            return "'%s'".formatted(safeValue);
        }
        return safeValue;
    }

    private static String escapeSpelString(String value) {
        if (value == null) return "";
        return value.replace("'", "''");
    }
}
