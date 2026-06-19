package com.traveler.useractivity.domain.process.filterrule.entity;

import com.traveler.common.db.entity.BaseEntity;
import com.traveler.useractivity.domain.process.core.entity.LogProcess;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FilterRule extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id", nullable = false)
    private LogProcess process;

    private String name;

    @Column(nullable = false, length = 1000)
    private String expression;

    //    @JdbcTypeCode(SqlTypes.JSON)
    //    private List<TokenDto> tokens;
    //
    //    @Column(
    //            columnDefinition = "TEXT"
    //    )
    //    private String tokensJson;
    //
    //    @Column(
    //            columnDefinition = "TEXT"
    //    )
    //    private String usedField;
    private boolean isActive;
}
