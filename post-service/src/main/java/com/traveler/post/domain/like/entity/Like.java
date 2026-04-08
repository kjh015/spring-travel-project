package com.traveler.post.domain.like.entity;

import com.traveler.common.db.entity.BaseEntity;
import com.traveler.post.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "likes",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_like_member_post",
                    columnNames = {"member_id", "post_id"})
        })
public class Like extends BaseEntity {
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
