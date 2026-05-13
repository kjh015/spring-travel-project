package com.traveler.member.domain.member.entity;

import com.traveler.common.db.entity.BaseEntity;
import com.traveler.member.domain.member.enums.Gender;
import com.traveler.member.global.exception.MemberServiceException;
import com.traveler.member.global.exception.code.MemberServiceErrorCode;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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
        name = "members",
        indexes = {@Index(name = "idx_member_deleted_at_status", columnList = "isDeleted, deletedAt")})
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String loginId;

    private String password;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 30)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Gender gender = Gender.NONE;

    private LocalDate birthDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    @Builder.Default
    private List<MemberRole> roles = new ArrayList<>();

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

    public void update(String nickname) {
        validateNotDeleted();
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        validateNotDeleted();
        this.password = password;
    }

    public Integer getAge() {
        if (this.birthDate == null) {
            return null;
        }
        return (int) ChronoUnit.YEARS.between(this.birthDate, LocalDate.now());
    }

    private void validateNotDeleted() {
        if (this.isDeleted) {
            throw new MemberServiceException(MemberServiceErrorCode.MEMBER_ALREADY_DELETED);
        }
    }
}
