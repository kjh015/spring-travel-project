package com.traveler.member.domain.member.entity;

import com.traveler.common.db.entity.BaseEntity;
import com.traveler.member.domain.member.enums.Gender;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
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
}
