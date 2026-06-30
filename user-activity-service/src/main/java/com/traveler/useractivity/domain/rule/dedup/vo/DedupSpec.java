package com.traveler.useractivity.domain.rule.dedup.vo;

import com.traveler.useractivity.domain.rule.dedup.enums.MatchType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;

public final class DedupSpec {

    private DedupSpec() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 하나의 중복 제거 단위
     */
    public record Rule(@NotEmpty @Valid List<Condition> conditions, @NotNull ExpirationTime expirationTime) {}

    /**
     * 개별 조건식
     */
    public record Condition(
            @NotBlank String field, // (예: "user_id")
            String value, // matchType이 ANY_VALUE일 때는 null 허용
            @NotNull MatchType matchType // (EXACT_MATCH / ANY_VALUE)
            ) {}

    /**
     * 만료 시간 정보
     */
    public record ExpirationTime(int days, int hours, int minutes, int seconds) {
        public long toTotalSeconds() {
            return Duration.ofDays(days)
                    .plusHours(hours)
                    .plusMinutes(minutes)
                    .plusSeconds(seconds)
                    .getSeconds();
        }
    }
}
