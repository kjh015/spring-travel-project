package com.traveler.post.domain.post.entity;

import com.traveler.common.db.entity.BaseEntity;
import com.traveler.post.domain.post.enums.Category;
import com.traveler.post.domain.post.enums.Region;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelPlace extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Region region;

    private String name;

    private String address;

    public void update(Category category, Region region, String name, String address) {
        this.category = category;
        this.region = region;
        this.name = name;
        this.address = address;
    }
}
