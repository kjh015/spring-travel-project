package com.traveler.useractivity.domain.process.formatrule.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.traveler.common.db.entity.BaseEntity;
import com.traveler.useractivity.domain.process.core.entity.LogProcess;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

@SuperBuilder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
@Table(
        name = "format_rule",
        indexes = {@Index(name = "idx_format_rule_deleted_at_status", columnList = "is_deleted, deleted_at")})
public class FormatRule extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_process_id", nullable = false)
    private LogProcess logProcess;

    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode defaultValues;

    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode fieldMappings;

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

    public void update(String name, JsonNode defaultValues, JsonNode fieldMappings, boolean isActive) {
        validateNotDeleted();
        this.name = name;
        this.defaultValues = defaultValues;
        this.fieldMappings = fieldMappings;
        this.isActive = isActive;
    }

    private void validateNotDeleted() {
        if (this.isDeleted) {
            throw new UserActivityServiceException(UserActivityServiceErrorCode.FORMAT_RULE_ALREADY_DELETED);
        }
    }
}
