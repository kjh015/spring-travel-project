package com.traveler.useractivity.domain.filter.entity;

import com.traveler.common.db.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Filter extends BaseEntity {
    @Column
    private String exampleColumn;

    void update(String exampleColumn) {
        this.exampleColumn = exampleColumn;
    }
}
