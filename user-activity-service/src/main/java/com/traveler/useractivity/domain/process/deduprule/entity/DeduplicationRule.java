package com.traveler.useractivity.domain.process.deduprule.entity;

import com.traveler.common.db.entity.BaseEntity;
import com.traveler.useractivity.domain.process.core.entity.LogProcess;
import com.traveler.useractivity.domain.process.deduprule.vo.DeduplicationSpec;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@SuperBuilder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeduplicationRule extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_process_id", nullable = false)
    private LogProcess logProcess;

    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<DeduplicationSpec.Rule> rules;

    private boolean isActive;

    @Builder.Default
    private boolean isDeleted = false;

    private Instant deletedAt;

    public void delete() {
        if (this.isDeleted) {
            return;
        }
        this.isDeleted = true;
        this.deletedAt = Instant.now();
    }

    public void update(String name, List<DeduplicationSpec.Rule> rules, boolean isActive) {
        validateNotDeleted();
        this.name = name;
        this.rules = rules;
        this.isActive = isActive;
    }

    private void validateNotDeleted() {
        if (this.isDeleted) {
            throw new UserActivityServiceException(UserActivityServiceErrorCode.DEDUP_RULE_ALREADY_DELETED);
        }
    }
}
