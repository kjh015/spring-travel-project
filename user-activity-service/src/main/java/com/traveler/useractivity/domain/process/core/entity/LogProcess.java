package com.traveler.useractivity.domain.process.core.entity;

import com.traveler.common.db.entity.BaseEntity;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

@SuperBuilder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
@Table(
        name = "log_process",
        indexes = {@Index(name = "idx_log_process_deleted_at_status", columnList = "is_deleted, deleted_at")})
public class LogProcess extends BaseEntity {
    private String name;
    private String description;

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

    public void update(String name, String description) {
        validateNotDeleted();
        this.name = name;
        this.description = description;
    }

    private void validateNotDeleted() {
        if (this.isDeleted) {
            throw new UserActivityServiceException(UserActivityServiceErrorCode.FORMAT_RULE_ALREADY_DELETED);
        }
    }
}
