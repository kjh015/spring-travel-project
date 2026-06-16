package com.traveler.member.domain.member.entity;

import com.traveler.common.db.entity.BaseEntity;
import com.traveler.member.domain.member.enums.RoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_member_role_member_id_role_type",
                    columnNames = {"member_id", "role_type"})
        })
public class MemberRole extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private RoleType roleType = RoleType.ROLE_USER;
}
